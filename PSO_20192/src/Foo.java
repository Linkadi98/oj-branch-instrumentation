/*
 * This code was generated by ojc.
 */
public class Foo
{

    private void printSomething( int a )
    {
        trace.add( new java.lang.Integer( 1 ) );
        if (a < 3) {
            trace.add( new java.lang.Integer( 2 ) );
            System.out.println( "xxx" );
        } else {
            trace.add( new java.lang.Integer( 3 ) );
            if (a > 4) {
                trace.add( new java.lang.Integer( 4 ) );
                System.out.println( "yyy" );
            } else {
                trace.add( new java.lang.Integer( 5 ) );
                if (a == 3) {
                    trace.add( new java.lang.Integer( 6 ) );
                    System.out.println( "zzz" );
                } else {
                    trace.add( new java.lang.Integer( 7 ) );
                    System.out.println( "ttt" );
                }
            }
        }
    }

    
    static java.util.Set trace = new java.util.HashSet();

    
    public static void newTrace()
    {
        trace = new java.util.HashSet();
    }

    
    public static java.util.Set getTrace()
    {
        return trace;
    }

}