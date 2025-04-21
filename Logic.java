/********************************************************************
 * Logic class to handle the calculation of the equations
 *
 * @author Aniekan Ekarika
 * @version v1 max calc is cubic
 *******************************************************************/
public class Logic
{
    protected int polynomialDegree;
    protected int[] coefficients;
    
    public Logic()
    {
        polynomialDegree = 3;
        coefficients = new int[4];
    }
    
    public void set(String LHS, String RHS)
    {
        // get the cooefficients for left hand side and right hand side
        int[] leftHandCoef = getCoefficients(LHS);
        int[] rightHandCoef = getCoefficients(RHS);
        
        for(int i=0; i<coefficients.length; i++)
        {
            coefficients[i] = leftHandCoef[i] - rightHandCoef[i];
        }
        
        polynomialDegree=3;
        if (coefficients[0] == 0)
        {
            polynomialDegree--;
            
            if (coefficients[1] == 0)
            {
                polynomialDegree--;
                
                if (coefficients[2] == 0)
                {
                    polynomialDegree--;
                }
            }
        }
    }
    
    public String calculate()
    {
        String result = "";
        
        result += getEquation() + "\n";
        
        switch (polynomialDegree)
        {
            case 1:
                result += linearSolve();
                break;
            case 2:
                result += quadraticSolve();
                break;
            case 3:
                result += cubicSolve();
                break;
            default:
                result = "Error: your equaion is bs";
        }
        
        return result;
    }
    
    public String linearSolve()
    {
        double coefficient = coefficients[2];
        double constant = coefficients[3];
        
        double x = (constant*-1)/coefficient;
        if (x==0) x=0;
        
        return String.format("Linear equation\nX = %.3f", x);
    }
        
    public String quadraticSolve()
    {
        String result = "";
        double a = coefficients[1];
        double b = coefficients[2];
        double c = coefficients[3];
        
        double discriminant = b*b - 4*a*c;
        if (discriminant == 0)
        {
            double x = (b*-1)/(2*a);
            
            if (x==0) x=0;
            
            result = String.format("Quadratic equation (equal roots)\nX = %.3f", x);
        }
        else if (discriminant > 0)
        {
            double x1 = (b*-1 + Math.sqrt(discriminant))/(2*a);
            double x2 = (b*-1 - Math.sqrt(discriminant))/(2*a);
            
            if (x1==0) x1=0;
            if (x2==0) x2=0;
            
            result = String.format("Quadratic equation (two distinct roots)\nX = %.3f\nX = %.3f", x1, x2);
        }
        else
        {
            double d = Math.abs(discriminant);
            
            double real = (b*-1)/(2*a);
            double imaginary = Math.sqrt(d)/(2*a);
            
            if (real==0) real=0;
            
            result = String.format("Quadratic equation (two imaginary roots)\nX = %.3f + %.3fi\nX = %.3f - %.3fi", real, imaginary, real, imaginary);
        }
        
        return result;
    }
    
    public String cubicSolve()
    {
        String result = "";
        
        double a = coefficients[0];
        double b = coefficients[1];
        double c = coefficients[2];
        double d = coefficients[3];
        
        double p = b / a;
        double q = c / a;
        double r = d / a;

        double A = q - (p * p) / 3.0;
        double B = r + (2 * p * p * p - 9 * p * q) / 27.0;

        double delta = (B * B) / 4.0 + (A * A * A) / 27.0;

        if (delta > 0) { 
            double sqrtDelta = Math.sqrt(delta);
            double u = Math.cbrt(-B / 2.0 + sqrtDelta);
            double v = Math.cbrt(-B / 2.0 - sqrtDelta);
            double realRoot = u + v - p / 3.0;
            
            double realPart = -(u + v) / 2.0 - p / 3.0;
            double imagPart = Math.abs((u - v)) * Math.sqrt(3) / 2.0;
            
            result = String.format("Cubic equation (one real, two complex)\n" +
                               "X = %.3f\n" +
                               "X = %.3f + %.3fi\n" +
                               "X = %.3f - %.3fi",
                               realRoot, realPart, imagPart, realPart, imagPart);
        } 
        else if (delta < 0) { // Three distinct real roots
            double theta = Math.acos(-B / (2 * Math.sqrt(-A * A * A / 27.0)));
            double t = 2 * Math.sqrt(-A / 3.0);
            
            double root1 = t * Math.cos(theta / 3.0) - p / 3.0;
            double root2 = t * Math.cos((theta + 2 * Math.PI) / 3.0) - p / 3.0;
            double root3 = t * Math.cos((theta + 4 * Math.PI) / 3.0) - p / 3.0;
            
            result = String.format("Cubic equation (three distinct)\n" +
                               "X = %.3f\n" +
                               "X = %.3f\n" +
                               "X = %.3f",
                               root1, root2, root3);
        } 
        else { // delta == 0: multiple roots
            double root1 = Math.cbrt(-B / 2.0) * 2 - p / 3.0;
            double root2 = -Math.cbrt(-B / 2.0) - p / 3.0;
            
            if (Math.abs(root1 - root2) < 1e-6) {
                result = String.format("Cubic equation (triple real root)\n" +
                                   "X = %.3f", root1);
            } else {
                result = String.format("Cubic equation (single and double)\n" +
                                   "X = %.3f\n" +
                                   "X = %.3f\n" +
                                   "X = %.3f",
                                   root1, root2, root2);
            }
        }
        
        return result;
    }
    
    public int[] getCoefficients(String equation)
    {
        int[] result = new int[4];
        String[] res;
        equation = equation.replaceAll(" ", "");
        
        result[0] = 0;
        while(equation.contains("x^3"))
        {
            res = getCoefficient(equation, "x^3");
            result[0] += Integer.parseInt(res[0]);
            equation = res[1];
        }
                
        // get coefficient of x^2 if it exists
        result[1] = 0;
        while(equation.contains("x^2"))
        {
            res = getCoefficient(equation, "x^2");
            result[1] += Integer.parseInt(res[0]);
            equation = res[1];
        }
                
        // get coefficient of x if it exists
        result[2] = 0;
        while(equation.contains("x"))
        {
            res = getCoefficient(equation, "x");
            result[2] += Integer.parseInt(res[0]);
            equation = res[1];
        }
    
        // get the constant
        result[3] = 0;
        int i=-1;
        String number;
        while(!equation.isEmpty())
        {
            i++;
            if (i != equation.length()-1)
            {
                if (Character.isDigit(equation.charAt(i)) && !Character.isDigit(equation.charAt(i+1)))
                {
                    number = equation.substring(0, i + 1);
                    result[3] += Integer.parseInt(number);
                    equation = equation.substring(i+1);
                    i=-1;
                }
            }
            else
            {
                result[3] += Integer.parseInt(equation);
                equation = "";
            }
        }
        
        return result;
    }
    
    public String[] getCoefficient(String equation, String search)
    {
        String result;
        boolean  hasSign = false;
        int indexOfSearch = equation.indexOf(search);
        int start = indexOfSearch-1;

        while(start >= 0 && !hasSign)
        {
            if (equation.charAt(start) == '+' || equation.charAt(start) == '-')
            {
                hasSign = true;
            }
            start--;
        }
        
        String coeffStr = equation.substring(start+1, indexOfSearch);
        
        String remaining = equation.substring(0, start+1) + equation.substring(indexOfSearch + search.length());
        
        if (coeffStr.isEmpty()) 
        {
            result = "1";
        }
        else if (coeffStr.equals("+"))
        {
            result = "1";
        }
        else if (coeffStr.equals("-"))
        {
            result = "-1";
        }
        else
        {   
            result = coeffStr;
        }

        return new String[]{result, remaining};
    }
    
    public String getEquation()
    {
        String result = "";
        String[] terms = {"x^3", "x^2", "x"};
        int coefficient;
        int constant;
        boolean first = true;
        
        for (int i=0; i<coefficients.length-1; i++)
        {
            coefficient = coefficients[i];
            if(coefficient!=0)
            {
                if (first)
                {
                    if (coefficient == -1)
                    {
                        result += "-" + terms[i];
                    }
                    else if (coefficient == 1)
                    {
                        result += terms[i];
                    }
                    else if(coefficient > 0)
                    {
                        result += coefficient + terms[i] ;
                    }
                    else if (coefficient < 0)
                    {
                        result += "-" + Math.abs(coefficient) + terms[i];
                    }
                    first = false;
                }
                else
                {
                    if (coefficient == -1)
                    {
                        result += " - " + terms[i];
                    }
                    else if (coefficient == 1)
                    {
                        result += " + " + terms[i];
                    }
                    else if(coefficient > 0)
                    {
                        result += " + " + coefficient + terms[i] ;
                    }
                    else if (coefficient < 0)
                    {
                        result += " - " + Math.abs(coefficient) + terms[i];
                    }
                }
            }
        }
        
        constant = coefficients[3]; 
        if (constant < 0)
        {
            result += " - " + Math.abs(constant);
        }
        else if (constant > 0)
        {
            result += " + " + constant;
        }
        
        result += " = 0";
        
        
        return result;
    }
}
