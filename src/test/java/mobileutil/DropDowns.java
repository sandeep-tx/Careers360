package mobileutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DropDowns {
    public enum Protocol {
        Alliance("Alliance"),
        Maytag("Maytag"),
        LG("LG"),
        Pulse("Pulse");
        private String name;

        Protocol(String name) {
            this.name = name;
        }


        public String getName() {
            return this.name;
        }
    }

    public enum Category {
        CarWash("Car Wash", "1"),
        Amusement("Amusement", "2"),
        Laundry("Laundry", "3"),
        Merchant("Merchant", "4"),
        Parking("Parking", "5"),
        Tips("Tips", "6"),
        Transit("Transit", "7"),
        Vending("Vending", "8"),
        Other("Other", "9");

        private String name;
        private String index;

        Category(String name, String index) {
            this.name = name;
            this.index = index;
        }


        public String getName() {
            return this.name;
        }

        public String getIndex() {
            return this.index;
        }

    }

    public enum CarWashSubCategory {
        SelfServiceBay("Self Service Bay"),
        WashBay("Wash Bay"),
        Vacuum("Vacuum"),
        Other("Other");

        private String name;

        CarWashSubCategory(String name) {
            this.name = name;
        }


        public String getName() {
            return this.name;
        }
    }

    public enum VendingSubCategory {
        HotBeverage("Hot Beverage"),
        ColdBeverage("Cold Beverage"),
        Snack("Snack"),
        FreshFood("Fresh Food"),
        FrozenFood("Frozen Food"),
        Other("Other");

        private String name;

        VendingSubCategory(String name) {
            this.name = name;
        }


        public String getName() {
            return this.name;
        }
    }

    public enum LaundrySubCategory {
        Washer("Washer"),
        Dryer("Dryer"),
        Combo("Combo"),
        StackDryer("Stack Dryer"),
        Vending("Vending"),
        Other("Other");


        private String name;

        LaundrySubCategory(String name) {
            this.name = name;
        }


        public String getName() {
            return this.name;
        }
    }

    public enum AmusementSubCategory {
        ArcadeVideo("Arcade/Video"),
        Pinball("Pinball"),
        Redemption("Redemption"),
        Merchandise("Merchandise"),
        DartSkill("Dart/Skill"),
        Pool("Pool"),
        Other("Other");

        private String name;

        AmusementSubCategory(String name) {
            this.name = name;
        }


        public String getName() {
            return this.name;
        }
    }

    public enum ServiceLocation {
        Education("Education"),
        Retail("Retail"),
        TravelAndTourism("Travel and Tourism"),
        Industrial("Industrial"),
        Transportation("Transportation"),
        Office("Office"),
        Construction("Construction"),
        Entertainment("Entertainment"),
        FoodService("Food Service"),
        Housing("Housing"),
        Laundromats("Laundromats"),
        CarWash("Car Wash"),
        Other("Other");
        private String name;

        ServiceLocation(String name) {
            this.name = name;
        }


        public String getName() {
            return this.name;
        }
    }

    public static Map<String, Object> getEnumMap() {
        Map<String, Object> enumMap = new HashMap<>();

        List<String> categoryMenuList = new ArrayList<>();
        for (DropDowns.Category data : DropDowns.Category.values()) {
            categoryMenuList.add(data.getName());
        }
        enumMap.put("Category", categoryMenuList);

        List<String> carWashSubCategoryMenuList = new ArrayList<>();
        for (DropDowns.CarWashSubCategory data : DropDowns.CarWashSubCategory.values()) {
            carWashSubCategoryMenuList.add(data.getName());
        }
        enumMap.put("Car Wash", carWashSubCategoryMenuList);
        enumMap.put("CarWash", carWashSubCategoryMenuList);

        List<String> vendingSubCategoryMenuList = new ArrayList<>();
        for (DropDowns.VendingSubCategory data : DropDowns.VendingSubCategory.values()) {
            vendingSubCategoryMenuList.add(data.getName());
        }
        enumMap.put("Vending", vendingSubCategoryMenuList);

        List<String> laundrySubCategoryMenuList = new ArrayList<>();
        for (DropDowns.LaundrySubCategory data : DropDowns.LaundrySubCategory.values()) {
            laundrySubCategoryMenuList.add(data.getName());
        }
        enumMap.put("Laundry", laundrySubCategoryMenuList);


        List<String> amusementSubCategoryMenuList = new ArrayList<>();
        for (DropDowns.AmusementSubCategory data : DropDowns.AmusementSubCategory.values()) {
            amusementSubCategoryMenuList.add(data.getName());
        }
        enumMap.put("Amusement", amusementSubCategoryMenuList);

        List<String> serviceLocationSubCategoryMenuList = new ArrayList<>();
        for (DropDowns.ServiceLocation data : DropDowns.ServiceLocation.values()) {
            serviceLocationSubCategoryMenuList.add(data.getName());
        }
        enumMap.put("Service Location", serviceLocationSubCategoryMenuList);
        enumMap.put("ServiceLocation", serviceLocationSubCategoryMenuList);


        return enumMap;


    }


}
