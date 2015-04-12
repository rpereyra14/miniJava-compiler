//Test 2 for for-loop
//no curly brackets after for loop, test that i got updated

class MainClass{

	public static void main( String[] args ){
		MainClass a = new MainClass();
		int i = 0;
		for( i = 6; i < 9; i = i + 1 )
			a.nothing( i );
		if( i != 0 ){
			System.out.println( 3 );
		}
	}

	public int nothing( int i ){
		return i;
	}

}