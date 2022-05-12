import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    static final String patientPath ="src\\main\\resources\\patients.xml";

    public static void main(String[] args) {

        Functions patientFunct = new Functions();
        Scanner scn = new Scanner(System.in);
        ArrayList<Patient> patients = new ArrayList<>();
        int choice;
        boolean r =patientFunct.read(patientPath, patients);
        boolean b = r;
        while (r) {

            System.out.print("Válassza ki mit szeretne tenni(a menüpont számát adja meg):\n" +
                    "\t1- Listázás\n" +
                    "\t2- Új elem\n" +
                    "\t3- Elem módosítása\n" +
                    "\t4- Elem törlése\n" +
                    "\t0- Kilépés\n");
            System.out.print("Választás: ");
            try {
                choice = scn.nextInt();
            } catch (InputMismatchException ex) {
                scn.nextLine();
                System.err.println("Hibás adat");
                choice = -1;
            }
            switch (choice) {
                case 0:
                    r = false;
                    break;
                case 1:
                    if (patients.size() != 0) {
                        patientFunct.listPatients(patients);
                    } else {
                        System.out.println("Nincsenek páciensek.");
                    }
                    break;
                case 2:
                    patientFunct.newPatient(patients);
                    break;
                case 3:
                    patientFunct.modifyPatient(patients);
                    break;
                case 4:
                    patientFunct.delPatient(patients);
                    break;
                default:
                    break;
            }
        }
        if (b) {
            patientFunct.save(patients, patientPath);
        }
    }
}