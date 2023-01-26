package com.example.fyphomeowner;

import android.os.Parcel;
import android.os.Parcelable;

public class Objects {

}

//enum Role{
//    superAdmin("Super Admin", "Admin account which manages all the accounts in the system"),
//    companyAdmin("Company Admin", "An admin account of a registered company which manages all the company accounts"),
//    customerService("Customer service Staff", "A Staff account whose role is to manage customer service request and handle maintenance task delegation to technicians"),
//    technician("Technician Staff", "A Staff account whose role is to manage equipment, chemicals, and retreiving homeowner water usage levels"),
//    homeowner("Homeowner", "A homeowner's account which has access to their respective water usage, profile details, subscription details, and businesses available");
//
//    public final String label;
//    public final String description;
//
//    private Role(String label, String description){
//        this.label = label;
//        this.description = description;
//    }
//
//    public String getLabel() {
//        return label;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    @Override
//    public String toString() {
//        return label;
//    }
//}

//class User{
//    private int ID;
//    private String name;
//    private String email;
//    private String password;
//
//    public User(){
//    }
//
//    public User(int ID,String name, String email, String password) {
//        this.ID = ID;
//        this.name = name;
//        this.email = email;
//        this.password = password;
//    }
//
//    public User(User user){
//        this.ID = user.getID();
//        this.name = user.getName();
//        this.email = user.getEmail();
//        this.password = user.getPassword();
//    }
//
//    public static int getInstanceCounter() {
//        return instanceCounter;
//    }
//
//    public static void setInstanceCounter(int instanceCounter) {
//        User.instanceCounter = instanceCounter;
//    }
//
//    public int getID() {
//        return ID;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public void setEmail(String email) {
//        this.email = email;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }
//
//    public Status getStatus() {
//        return status;
//    }
//
//    public void setStatus(Status status) {
//        this.status = status;
//    }
//
//    public Role getType() {
//        return type;
//    }
//
//    public void setType(Role type) {
//        this.type = type;
//    }
//
//    @Override
//    public String toString() {
//        return "user{" +
//                "ID=" + ID +
//                ", name='" + name + '\'' +
//                ", email='" + email + '\'' +
//                ", password='" + password + '\'' +
//                ", status=" + status +
//                ", type=" + type +
//                '}';
//    }
//}

//class Homeowner extends User{
//    private int phoneNo;
//    private String street;
//    private String blkNo;
//    private String unitNo;
//    private int postalCode;
//    private HouseType houseType;
//    private int householdSize;
//    private int subscribedCompanyUEN;
//
//    Homeowner(){
//        super();
//    }
//
//    public Homeowner(int phoneNo, String street, String blkNo, String unitNo, int postalCode, com.example.fyphomeowner.HouseType houseType, int householdSize, Company subscribedCompany) {
//        this.phoneNo = phoneNo;
//        this.street = street;
//        this.blkNo = blkNo;
//        this.unitNo = unitNo;
//        this.postalCode = postalCode;
//        this.houseType = houseType;
//        this.householdSize = householdSize;
//        this.subscribedCompanyUEN = subscribedCompany.getUEN();
//    }
//
//    public Homeowner(String name, String email, String password, Status status, Role type, int phoneNo, String street, String blkNo, String unitNo,
//                     int postalCode, HouseType houseType, int householdSize, Company subscribedCompany) {
//        super(name, email, password, status, type);
//        this.phoneNo = phoneNo;
//        this.street = street;
//        this.blkNo = blkNo;
//        this.unitNo = unitNo;
//        this.postalCode = postalCode;
//        this.houseType = houseType;
//        this.householdSize = householdSize;
//        this.subscribedCompanyUEN = subscribedCompany.getUEN();
//    }
//
//    public Homeowner(User user, int phoneNo, String street, String blkNo, String unitNo, int postalCode,
//                     com.example.fyphomeowner.HouseType houseType, int householdSize, Company subscribedCompany) {
//        super(user);
//        this.phoneNo = phoneNo;
//        this.street = street;
//        this.blkNo = blkNo;
//        this.unitNo = unitNo;
//        this.postalCode = postalCode;
//        this.houseType = houseType;
//        this.householdSize = householdSize;
//        this.subscribedCompanyUEN = subscribedCompany.getUEN();
//    }
//
//    public int getPhoneNo() {
//        return phoneNo;
//    }
//
//    public void setPhoneNo(int phoneNo) {
//        this.phoneNo = phoneNo;
//    }
//
//    public String getStreet() {
//        return street;
//    }
//
//    public void setStreet(String street) {
//        this.street = street;
//    }
//
//    public String getBlkNo() {
//        return blkNo;
//    }
//
//    public void setBlkNo(String blkNo) {
//        this.blkNo = blkNo;
//    }
//
//    public String getUnitNo() {
//        return unitNo;
//    }
//
//    public void setUnitNo(String unitNo) {
//        this.unitNo = unitNo;
//    }
//
//    public int getPostalCode() {
//        return postalCode;
//    }
//
//    public void setPostalCode(int postalCode) {
//        this.postalCode = postalCode;
//    }
//
//    public com.example.fyphomeowner.HouseType getHouseType() {
//        return houseType;
//    }
//
//    public void setHouseType(com.example.fyphomeowner.HouseType houseType) {
//        this.houseType = houseType;
//    }
//
//    public int getHouseholdSize() {
//        return householdSize;
//    }
//
//    public void setHouseholdSize(int householdSize) {
//        this.householdSize = householdSize;
//    }
//
//    public int getSubscribedCompanyUEN() {
//        return subscribedCompanyUEN;
//    }
//
//    public void setCompanySubscribed(Company company) {
//        this.subscribedCompanyUEN = company.getUEN();
//    }
//
//    @Override
//    public String toString() {
//        return "Homeowner{" +
//                "phoneNo=" + phoneNo +
//                ", street='" + street + '\'' +
//                ", blkNo=" + blkNo +
//                ", unitNo=" + unitNo +
//                ", postalCode=" + postalCode +
//                ", houseType=" + houseType +
//                ", householdSize=" + householdSize +
//                ", companySubscribed=" + subscribedCompanyUEN +
//                '}';
//    }
//}

class Ticket{
    private Integer ID;
    private String date;
    private String description;
    private String status;

    public Ticket(Integer ID, String date, String description, String status) {
        this.ID = ID;
        this.date = date;
        this.description = description;
        this.status = status;
    }

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class Company implements Parcelable {
    private Integer ID;
    private String name;
    private Integer PhoneNo;
    private String street;
    private Integer postalCode;
    private String Description;
    private Integer noOfStars;

    public Company(){

    }

    public Company(Integer ID, String name, Integer phoneNo, String street, Integer postalCode, String description, Integer noOfStars) {
        this.ID = ID;
        this.name = name;
        this.PhoneNo = phoneNo;
        this.street = street;
        this.postalCode = postalCode;
        this.Description = description;
        this.noOfStars = noOfStars;
    }

    public Company (Company company){
        this.name = company.getName();
        this.PhoneNo = company.getPhoneNo();
        this.street = company.getStreet();
        this.postalCode = company.getPostalCode();
        this.Description = company.getDescription();
        this.noOfStars = company.getNoOfStars();
    }

    //Parcelable constructor
    //Has to be the same order as the writeToParcel method**
    protected Company(Parcel in) {
        name = in.readString();
        PhoneNo = in.readInt();
        street = in.readString();
        postalCode = in.readInt();
        Description = in.readString();
        noOfStars = in.readInt();
    }

    //Create parcelables
    public static final Creator<Company> CREATOR = new Creator<Company>() {
        @Override
        public Company createFromParcel(Parcel in) {
            return new Company(in);
        }

        @Override
        public Company[] newArray(int size) {
            return new Company[size];
        }
    };

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPhoneNo() {
        return PhoneNo;
    }

    public void setPhoneNo(Integer phoneNo) {
        PhoneNo = phoneNo;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public Integer getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(Integer postalCode) {
        this.postalCode = postalCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public Integer getNoOfStars() {
        return noOfStars;
    }

    public void setNoOfStars(Integer noOfStars) {
        this.noOfStars = noOfStars;
    }


    @Override
    public String toString() {
        return "Company{" +
                ", ID='" + ID + '\'' +
                ", name='" + name + '\'' +
                ", PhoneNo=" + PhoneNo +
                ", street='" + street + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", Description='" + Description + '\'' +
                ", noOfStars='" + noOfStars + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Parcelable adds class attributes into the parcel
    //Has to be the same order as the Parcelable constructor method**
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(street);
        parcel.writeString(Description);

        if(ID!=null){
            parcel.writeInt(ID);
        }else{
            parcel.writeString(null);
        }
        if(PhoneNo!=null){
            parcel.writeInt(PhoneNo);
        }else{
            parcel.writeString(null);
        }
        if(postalCode!=null){
            parcel.writeInt(postalCode);
        }else{
            parcel.writeString(null);
        }
        if(noOfStars!=null){
            parcel.writeInt(noOfStars);
        }else{
            parcel.writeString(null);
        }

    }
}
