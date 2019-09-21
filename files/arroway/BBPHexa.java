/*
 * Java program a pí hexadecimális alakjának kiszámításására a BBP
 * algoritmussal.
 * Készült a következő cikk alapján:
 *	 	https://www.davidhbailey.com//dhbpapers/bbp-alg.pdf
 *
 * Forrás:
 *		https://www.tankonyvtar.hu/hu/tartalom/tkt/javat-tanitok-javat/apbs02.html#pi_jegyei
 */

import java.lang.Math;		// pow()

public class BBPHexa {
	String d16PiHexaJegyek;

	public BBPHexa(int d) {
		double d16Pi = 0;

		double d16S1 = d16Sj(d, 1);
		double d16S4 = d16Sj(d, 4);
		double d16S5 = d16Sj(d, 5);
		double d16S6 = d16Sj(d, 6);

		d16Pi = 4 * d16S1 - 2 * d16S4 - d16S5 - d16S6;

		StringBuffer sb = new StringBuffer();

		Character hexa[] = {'A', 'B', 'C', 'D', 'E', 'F'};

		while(d16Pi != 0) {
			int jegy = (int)StrictMath.floor(16*d16Pi);

			if(jegy < 10) {
				sb.append(jegy);
				System.out.println(jegy);
			} else {
				sb.append(hexa[jegy-10]);
			}

			d16Pi = (16 * d16Pi) - StrictMath.floor(16 * d16Pi);
		}

		d16PiHexaJegyek = sb.toString();
	}


	public double d16Sj(int d, int j) {
		double d16Sj = 0;

		for(int k = 0; k <= d; k++) {
			d16Sj += (double) n16modk(d - k, 8 * k + j) / (double) (8 * k + j);
		}

		return d16Sj - StrictMath.floor(d16Sj);
	}

	public long n16modk(int n, int k) {
        int t = 1;

        while(t <= n)
            t *= 2;

        long r = 1;

        while(true) {

            if(n >= t) {
                r = (16*r) % k;
                n = n - t;
            }

            t = t/2;

            if(t < 1)
                break;

            r = (r*r) % k;

        }

        return r;
    }

	public String toString() {
		return d16PiHexaJegyek;
	}

	public static void main(String[] args) {
		BBPHexa obj = new BBPHexa(1000000);
		System.out.println(obj.toString());
	}
}
