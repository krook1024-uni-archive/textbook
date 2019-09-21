/*
 * Java program a pí decimális alakjának kiszámítására a BBP algoritmussal.
 *
 * Készült a következo cikk alapján;
 *	 		https://www.davidhbailey.com//dhbpapers/bbp-alg.pdf
 *
 * @author Molnár Antal
 */

import java.lang.Math;		// pow()

public class BBP {
	int precision;
	long double pi, v, w, x, y, z;

	public BBP(int precision) {
		this.precision = precision;
	}

	public double compute() {

		double tmp;

		for(int i=0; i < this.precision; i++) {
			tmp = 8 * i;

			this.v = 1 / (Math.pow(16, i));
			this.w = 4 / (tmp + 1);
			this.x = 2 / (tmp + 4);
			this.y = 1 / (tmp + 5);
			this.z = 1 / (tmp + 6);

			this.pi += this.v * ( this.w - this.x - this.y - this.z );
		}

		return this.pi;
	}

	public static void main(String[] args) {
		BBP obj = new BBP(5000000);
		System.out.println(obj.compute());
	}
}
