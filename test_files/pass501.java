//Test 1 for for-loop
//var decl outside initializing statement, i should exist outside for-loop and retain its value from inside the loop

class MainClass{

	public static void main( String[] args ){
		int i = 0;
		for( i = 6; i > 1; i = i - 1 ){
			int j = 1 + 1;
			j = i;
		}
		System.out.println(i);
	}

}