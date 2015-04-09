import java.util.Scanner;


public class Menu {
	static int choice;
	static boolean booleanabe = true;
	public static void main(String[] args) {
		System.out.println("Velkommen til min mor. Du har nu følgende valgmuligheder:");
		System.out.println(" ");
		System.out.println("1. Print filer i arbejdsmappen");
		System.out.println(" ");
		System.out.println("2. Skift arbejdsmappe");
		System.out.println(" ");
		System.out.println("3. hent fil fra server");
		System.out.println(" ");
		System.out.println("4. Quit");
		

		Scanner scan = new Scanner(System.in);
		
		while(booleanabe){
			try {
				choice = scan.nextInt();
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("du har ikke indtastet et tal der repræsentere mulighederne. prøv igen");
				break;
			}
			switch (choice) {
			case 1:
				System.out.println("Du er nu kommet ind i 1");
				booleanabe = false;
				break;
			case 2:
				System.out.println("Du er nu kommet ind i 2");
				booleanabe = false;
				break;
			case 3:
				System.out.println("Du er nu kommet ind i 3");
				booleanabe = false;
				break;
				
			case 4:
				booleanabe = false;
				break;
				
			default:
				System.out.println("Du har indtastet en forkert mulighed. prøv igen");
				break;
			}
		}
	}
}