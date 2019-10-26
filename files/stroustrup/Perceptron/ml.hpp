
/**
 * @brief JUDAH - Jacob is equipped with a text-based user interface
 *
 * @file ql.hpp
 * @author  Norbert Bátfai <nbatfai@gmail.com>
 * @version 0.0.1
 *
 * @section LICENSE
 *
 * Copyright (C) 2015 Norbert Bátfai, batfai.norbert@inf.unideb.hu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * @section DESCRIPTION
 *
 * JACOB, https://github.com/nbatfai/jacob
 *
 * "The son of Isaac is Jacob." The project called Jacob is an experiment
 * to replace Isaac's (GUI based) visual imagination with a character console.
 *
 * ISAAC, https://github.com/nbatfai/isaac
 *
 * "The son of Samu is Isaac." The project called Isaac is a case study
 * of using deep Q learning with neural networks for predicting the next
 * sentence of a conversation.
 *
 * SAMU, https://github.com/nbatfai/samu
 *
 * The main purpose of this project is to allow the evaluation and
 * verification of the results of the paper entitled "A disembodied
 * developmental robotic agent called Samu Bátfai". It is our hope
 * that Samu will be the ancestor of developmental robotics chatter
 * bots that will be able to chat in natural language like humans do.
 *
 */
#ifndef ML_HPP // include guard
#define ML_HPP

#include <iostream>
#include <cstdarg>
#include <map>
#include <iterator>
#include <cmath>
#include <random>
#include <limits>
#include <fstream>

class Perceptron
{
public:
    Perceptron ( int nof, ... )
    {
        n_layers = nof;

        units = new double*[n_layers];
        n_units = new int[n_layers];

        va_list vap;

        va_start ( vap, nof );

        for ( int i {0}; i < n_layers; ++i )
        {
            n_units[i] = va_arg ( vap, int );

            if ( i )
                units[i] = new double [n_units[i]];
        }

        va_end ( vap );

        weights = new double**[n_layers-1];

        #ifndef RND_DEBUG
        std::random_device init;
        std::default_random_engine gen {init() };
        #else
        std::default_random_engine gen;
        #endif

        std::uniform_real_distribution<double> dist ( -1.0, 1.0 );

        for ( int i {1}; i < n_layers; ++i )
        {
            weights[i-1] = new double *[n_units[i]];

            for ( int j {0}; j < n_units[i]; ++j )
            {
                weights[i-1][j] = new double [n_units[i-1]];

                for ( int k {0}; k < n_units[i-1]; ++k )
                {
                    weights[i-1][j][k] = dist ( gen );
                }
            }
        }
    }

    Perceptron ( std::fstream & file )
    {
        file >> n_layers;

        units = new double*[n_layers];
        n_units = new int[n_layers];

        for ( int i {0}; i < n_layers; ++i )
        {
            file >> n_units[i];

            if ( i )
                units[i] = new double [n_units[i]];
        }

        weights = new double**[n_layers-1];

        for ( int i {1}; i < n_layers; ++i )
        {
            weights[i-1] = new double *[n_units[i]];

            for ( int j {0}; j < n_units[i]; ++j )
            {
                weights[i-1][j] = new double [n_units[i-1]];

                for ( int k {0}; k < n_units[i-1]; ++k )
                {
                    file >> weights[i-1][j][k];
                }
            }
        }
    }


    double sigmoid ( double x )
    {
        return 1.0/ ( 1.0 + exp ( -x ) );
    }


    double* operator() ( double image [] )
    {

        units[0] = image;

        for ( int i {1}; i < n_layers; ++i )
        {

            #ifdef CUDA_PRCPS

            cuda_layer ( i, n_units, units, weights );

            #else

            #pragma omp parallel for
            for ( int j = 0; j < n_units[i]; ++j )
            {
                units[i][j] = 0.0;

                for ( int k = 0; k < n_units[i-1]; ++k )
                {
                    units[i][j] += weights[i-1][j][k] * units[i-1][k];
                }

                units[i][j] = sigmoid ( units[i][j] );

            }

            #endif

        }

        //return sigmoid ( units[n_layers - 1][0] );

        for ( int i = 0; i < n_units[n_layers - 1]; ++i)
            image[i] = units[n_layers - 1][i];

        return image;
    }

    void learning ( double image [], double q, double prev_q )
    {
        double y[1] {q};

        learning ( image, y );
    }

    void learning ( double image [], double y[] )
    {
        //( *this ) ( image );

        units[0] = image;

        double ** backs = new double*[n_layers-1];

        for ( int i {0}; i < n_layers-1; ++i )
        {
            backs[i] = new double [n_units[i+1]];
        }

        int i {n_layers-1};

        for ( int j {0}; j < n_units[i]; ++j )
        {
            backs[i-1][j] = sigmoid ( units[i][j] ) * ( 1.0-sigmoid ( units[i][j] ) ) * ( y[j] - units[i][j] );

            for ( int k {0}; k < n_units[i-1]; ++k )
            {
                weights[i-1][j][k] += ( 0.2* backs[i-1][j] *units[i-1][k] );
            }

        }

        for ( int i {n_layers-2}; i >0 ; --i )
        {

            #pragma omp parallel for
            for ( int j =0; j < n_units[i]; ++j )
            {

                double sum = 0.0;

                for ( int l = 0; l < n_units[i+1]; ++l )
                {
                    sum += 0.19*weights[i][l][j]*backs[i][l];
                }

                backs[i-1][j] = sigmoid ( units[i][j] ) * ( 1.0-sigmoid ( units[i][j] ) ) * sum;

                for ( int k = 0; k < n_units[i-1]; ++k )
                {
                    weights[i-1][j][k] += ( 0.19* backs[i-1][j] *units[i-1][k] );
                }
            }
        }

        for ( int i {0}; i < n_layers-1; ++i )
        {
            delete [] backs[i];
        }

        delete [] backs;

    }

    ~Perceptron()
    {
        for ( int i {1}; i < n_layers; ++i )
        {
            for ( int j {0}; j < n_units[i]; ++j )
            {
                delete [] weights[i-1][j];
            }

            delete [] weights[i-1];
        }

        delete [] weights;

        for ( int i {0}; i < n_layers; ++i )
        {
            if ( i )
                delete [] units[i];
        }

        delete [] units;
        delete [] n_units;

    }

    void save ( std::fstream & out )
    {
        out << " "
        << n_layers;

        for ( int i {0}; i < n_layers; ++i )
            out << " " << n_units[i];

            for ( int i {1}; i < n_layers; ++i )
            {
                for ( int j {0}; j < n_units[i]; ++j )
                {
                    for ( int k {0}; k < n_units[i-1]; ++k )
                    {
                        out << " "
                        << weights[i-1][j][k];

                    }
                }
            }

    }

private:
    Perceptron ( const Perceptron & );
    Perceptron & operator= ( const Perceptron & );

    int n_layers;
    int* n_units;
    double **units;
    double ***weights;

};
#endif
