/*** referenced variable z is not declared
 * COMP 520
 * Identification
 */
class fail15 { 	
    public static void main(String[] args) {}

    int y;

    public void foo() {
	int x = y + z;
    }
}

