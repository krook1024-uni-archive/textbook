package com.krook1024;

public aspect Aspect {
    private int ones = 0, zeros = 0;

    // Az addbit() pointcut -- hogy tudjuk számolni a bemenő adatokat
    pointcut addbit(): execution(public void addBit(char));

    before(char b): addbit() && args(b) {
        if ('1' == b)
            ++ones;
        else if ('0' == b)
            ++zeros;
    }


    // A main() függvény pointcut -- hogy ki tudjuk írni a lefutás után az eredményt
    pointcut main(): execution(public static void main(String[]));

    after(): main() {
        System.out.println("ones = " + ones);
        System.out.println("zeros = " + zeros);
    }
}
