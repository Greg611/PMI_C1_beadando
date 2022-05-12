import java.util.ArrayList;
import java.util.Date;

public class Patient {
    private String ID;
    private String name;
    private Integer birthYear;
    private BloodTypeEnum bloodType;
    private Date checkInDate;
    private ArrayList<String> diseases;

    public Patient(){}

    public Patient(String ID, String name, Integer birthYear,String bloodType, Date checkInDate, ArrayList<String> diseases) {
        this.ID = ID;
        this.name = name;
        this.birthYear = birthYear;
        this.bloodType = BloodTypeEnum.findByValue(bloodType);
        this.checkInDate = checkInDate;
        this.diseases = diseases;
    }

    public String getID(){
        return ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getBirthYear() {
        return birthYear;
    }

    public void setBirthYear(Integer birthYear) {
        this.birthYear = birthYear;
    }

    public Date getCheckInDate() {
        return checkInDate;
    }

    public ArrayList<String> getDiseases() {
        return diseases;
    }

    public BloodTypeEnum getBloodType(){
        return this.bloodType;
    }

    public void setDiseaseByName(String disease,String newDisease) {
        if(disease.equals("")){
            this.diseases.add(newDisease);
        }
        else {
            for (int i = 0; i < this.diseases.size(); i++) {
                if (this.diseases.get(i).equals(disease)) {
                    if (newDisease.equals("")) {
                        this.diseases.remove(i);
                    } else {
                        this.diseases.set(i, newDisease);
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        String result =
        "Azonosító ='" + ID +'\'' +
                ", Név: '" + name + '\'' +
                ", Születési év: '" + birthYear + '\'' +
                ", Vércsoport: '" + bloodType.getShortName() + '\'' +
                ", Felvétel dátuma: '" + checkInDate + '\'' +
                ", Panaszok: '";
        for(int i=0;i< diseases.size();i++){
            if(i== diseases.size()-1){
                result = result + diseases.get(i) + '\'';
            }
            else{
                result = result + diseases.get(i) + ", ";
            }
        }
        return result;
    }
}
