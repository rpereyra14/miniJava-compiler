//Test 2 for for-loop
//test that stack is being cleared, test for-loop condition checking

class MainClass{

	public static void main( String[] args ){
		int i = 9;
		for( i = 6; i < 1500; i = i + 1 ){
			int j = i;
			i = i + 1;
		}
		if( i == 1500 ){
			System.out.println( 4 );
		}
	}

}