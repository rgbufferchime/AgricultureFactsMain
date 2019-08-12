package com.bufferchime.agriculturefact.Firebase;


public class FirebaseUserEntity {



        private String uId;

        private String email;

        private String name;

        private String city;

        private String phone;



        public FirebaseUserEntity(){
        }

        public FirebaseUserEntity(String uId, String email, String name, String city, String phone) {
            this.uId = uId;
            this.email = email;
            this.name = name;
            this.city = city;
            this.phone = phone;

        }

        public String getuId() {
            return uId;
        }

        public String getEmail() {
            return email;
        }

        public String getName() {
            return name;
        }

        public String getCity() {
            return city;
        }

        public String getPhone() {
            return phone;
        }


    }
