import java.util.Scanner;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import static java.lang.Integer.parseInt;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter f(x) in the form of X*X (not x^2): ");

        String function = scanner.nextLine();

        function = function.replace("X", "x"); // To normalize variable name

        System.out.println("Enter the value of a:");
        double a = scanner.nextDouble();

        System.out.println("Enter the value of b:");
        double b = scanner.nextDouble();

        System.out.println("Enter the value of tol:");
        double tol = scanner.nextDouble();

        System.out.println("Enter the value of N for Newton Raphson Method and Fixed point Method:");
        int n = scanner.nextInt();

        double fa = evaluateFunction(function, a);

        double fb = evaluateFunction(function, b);

        Bisection(a, b, tol, fa, fb, function);

        Secant(a, b, tol, fa, fb, function);

        ModifiedSecant(a, b, tol, fa, fb, function);

       NewtonRaphson(a, tol, function, n);

        fixedPointMethod(a, tol, function, n);


    }



    //To process input function
    public static double evaluateFunction(String function, double x) {
        Expression exp = new ExpressionBuilder(function)
                .variable("x")
                .build()
                .setVariable("x", x);
        return exp.evaluate();
    }

    public static double evaluateDerivative(String function, double x) {
        double h = 1e-6; // Small step size
        return (evaluateFunction(function, x + h) - evaluateFunction(function, x - h)) / (2 * h);
    }

    //Bisection Method
    public static void Bisection(double a, double b, double tol, double fa, double fb, String function) {
        double c = 0;

        if (fa * fb > 0) // Can't be solved!!!!!
        {
            System.out.println("The function could not be solved F(a) * F(b) < 0");

            return;
        } else if (fa * fb <= 0) //Bisection
        {
            int N = (int) Math.ceil(Math.log((b - a) / tol) / Math.log(2));

            System.out.println("BISECTION METHOD");
            System.out.println("----------------");
            System.out.println("N = " + N);
            System.out.println("-----");

            System.out.printf("%-5s %-12s %-12s %-12s %-12s %-12s %-12s %-15s %-8s\n",
                    "i", "a", "b", "c", "f(a)", "f(b)", "f(c)", "|f(c)| < tol", "i=N");
            System.out.println("----------------------------------------------------------------------------------------------------");

            for (int i = 1; i <= N; i++) {
                c = (b + a) / 2;
                double fc = evaluateFunction(function, c);

                System.out.printf("%-5d %-12.4f %-12.4f %-12.4f %-12.4f %-12.4f %-12.4f %-15s %-8s\n",
                        i, a, b, c, fa, fb, fc, (Math.abs(fc) < tol) ? "Yes" : "No", (i == N) ? "Yes" : "No");

                // Check stopping conditions
                if (Math.abs(fc) < tol || i == N) {
                    System.out.println("----------------------------------------------------------------------------------------------------");

                    System.out.printf("x* = %.4f\n", c);

                    System.out.println("----------------------------------------------------------------------------------------------------");

                    break;
                }

                // Update a or b based on the sign of f(c)
                if (fa * fc < 0) {
                    fb = fc;

                    b = c; // Root is in [a, c]
                } else {
                    fa = fc;

                    a = c; // Root is in [c, b]
                }
            }
        }

    }

    //Secant Method
    public static void Secant(double a, double b, double tol, double fa, double fb, String function) {
        System.out.println("SECANT METHOD");
        System.out.println("-------------");

        int N = (int) Math.ceil(Math.log((b - a) / tol) / Math.log(2));

        double c;
        double fc;

        System.out.printf("%-5s %-12s %-12s %-12s %-12s %-12s %-12s %-15s %-8s\n",
                "i", "a", "b", "c", "f(a)", "f(b)", "f(c)", "|f(c)| < tol", "i=N");
        System.out.println("----------------------------------------------------------------------------------------------------");

        for (int i = 1; i <= N; i++) {

            c = b - fb * ((b - a) / (fb - fa));
            fc = evaluateFunction(function, c);

            System.out.printf("%-5d %-12.4f %-12.4f %-12.4f %-12.4f %-12.4f %-12.4f %-15s %-8s\n",
                    i, a, b, c, fa, fb, fc, (Math.abs(fc) < tol) ? "Yes" : "No", (i == N) ? "Yes" : "No");

            // Check stopping conditions
            if (Math.abs(fc) < tol || i == N) {
                System.out.println("----------------------------------------------------------------------------------------------------");
                System.out.printf("x* = %.4f\n", c);
                System.out.println("----------------------------------------------------------------------------------------------------");
                return;
            }

            // Shift values to the left: a = b, b = c
            a = b;

            fa = fb;

            b = c;

            fb = fc;
        }


    }

    //Modified Secant Method
    public static void ModifiedSecant(double a, double b, double tol, double fa, double fb, String function) {
        double c = 0;

        if (fa * fb > 0) // Can't be solved!!!!!
        {
            System.out.println("The function could not be solved F(a) * F(b) < 0");

            return;

        } else if (fa * fb <= 0) //Bisection
        {
            int N = (int) Math.ceil(Math.log((b - a) / tol) / Math.log(2));

            System.out.println("Modified Secant METHOD");
            System.out.println("----------------------");


            System.out.printf("%-5s %-12s %-12s %-12s %-12s %-12s %-12s %-15s %-8s\n",
                    "i", "a", "b", "c", "f(a)", "f(b)", "f(c)", "|f(c)| < tol", "i=N");
            System.out.println("----------------------------------------------------------------------------------------------------");

            for (int i = 1; i <= N; i++) {

                c = b - fb * ((b - a) / (fb - fa));
                double fc = evaluateFunction(function, c);

                System.out.printf("%-5d %-12.4f %-12.4f %-12.4f %-12.4f %-12.4f %-12.4f %-15s %-8s\n",
                        i, a, b, c, fa, fb, fc, (Math.abs(fc) < tol) ? "Yes" : "No", (i == N) ? "Yes" : "No");

                // Check stopping conditions
                if (Math.abs(fc) < tol || i == N) {

                    System.out.println("----------------------------------------------------------------------------------------------------");

                    System.out.printf("x* = %.4f\n", c);

                    System.out.println("----------------------------------------------------------------------------------------------------");
                    break;
                }

                // Update a or b based on the sign of f(c)
                if (fa * fc < 0) {
                    fb = fc;

                    b = c; // Root is in [a, c]
                } else {
                    fa = fc;

                    a = c; // Root is in [c, b]
                }
            }
        }
    }
    //Newton Raphson Method
    public static void NewtonRaphson(double x0, double tol, String function, int N) {

        System.out.println("NEWTON RAPHSON METHOD");
        System.out.println("---------------------");
        System.out.printf("%-5s %-10s %-15s %-15s %-15s %-5s%n",
                "i", "xi", "f(xi)", "f'(xi)", "|f(xi)| < tol", "i=N?");
        System.out.println("---------------------------------------------------------------");

        double xi = x0;

        for (int i = 1; i <= N; i++) {

            double fxi = evaluateFunction(function, xi); //To calculate f(xi)
            double fpxi = evaluateDerivative(function, xi);  //To calculate f'(xi)

            System.out.printf("%-5d %-10.4f %-15.4f %-15.4f %-15s %-5s%n",
                    i, xi, fxi, fpxi, (Math.abs(fxi) < tol) ? "Yes" : "No", (i == N) ? "Yes" : "No");

            //Stop condition
            if (Math.abs(fxi) < tol || i == N ) {
                System.out.println("---------------------------------------------------------------");
                System.out.printf("x* = %.4f\n", xi);
                System.out.println("---------------------------------------------------------------");
                return;
            }

            // Calculate xi+1
            xi = xi - (fxi / fpxi);
        }

        System.out.println("---------------------------------------------------------------");
        System.out.printf("x* = %.4f\n", xi);
    }

    public static void fixedPointMethod(double x0, double tol, String function, int N) {
        System.out.println("FIXED POINT METHOD");
        System.out.println("------------------");
        System.out.printf("%-5s %-10s %-15s %-15s %-15s %-5s%n",
                "i", "xi", "f(xi)", "g(xi)", "|f(xi)| < tol", "i=N?");
        System.out.println("--------------------------------------------------------------------------------------");

        double xi = x0;
        double last_gxi = xi;

        for (int i = 1; i <= N; i++) {
            double fxi = evaluateFunction(function, xi);
            double gxi = fxi + xi;
            last_gxi = gxi;

            System.out.printf("%-5d %-10.4f %-15.4f %-15.4f %-15s %-5s%n",
                    i, xi, fxi, gxi, (Math.abs(fxi) < tol) ? "Yes" : "No", (i == N) ? "Yes" : "No");

            if (Math.abs(fxi) < tol || i == N) {
                System.out.println("--------------------------------------------------------------------------------------");
                System.out.printf("x* = %.4f\n", last_gxi);
                System.out.println("--------------------------------------------------------------------------------------");
                return;
            }

            xi = gxi;
        }

        System.out.println("--------------------------------------------------------------------------------------");
        System.out.printf("x* = %.4f\n", last_gxi);
        System.out.println("--------------------------------------------------------------------------------------");
    }


}









