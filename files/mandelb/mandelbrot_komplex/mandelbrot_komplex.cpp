#include <png++/png.hpp>
#include <complex>

const int N = 500;
const int M = 500;
const double MAXX = 0.7;
const double MINX = -2.0;
const double MAXY = 1.35;
const double MINY = -1.35;

void GeneratePNG(const int tomb[N][M])
{
    png::image< png::rgb_pixel > image(N, M);
    for (int x = 0; x < N; x++)
    {
        for (int y = 0; y < M; y++)
        {
            image[x][y] = png::rgb_pixel(tomb[x][y], tomb[x][y], tomb[x][y]);
        }
    }
    image.write("kimenet.png");
}

int main()
{
    int tomb[N][M];

    double dx = ((MAXX - MINX) / N);
    double dy = ((MAXY - MINY) / M);

    std::complex<double> C, Z, Zuj;

    int iteracio;

    for (int i = 0; i < M; i++)
    {
        for (int j = 0; j < N; j++)
        {
			C = {MINX + j * dx , MAXY - i * dy};

            Z = 0;
            iteracio = 0;

            while(abs(Z) < 2 && iteracio++ < 255)
            {
                Zuj = Z*Z+C;
                Z = Zuj;
            }

            tomb[i][j] = 256 - iteracio;
        }
    }

    GeneratePNG(tomb);

    return 0;
}
