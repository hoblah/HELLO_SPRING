package com.kosmo.a30xmljsonparser02;

public class ActorVO {

    private String name;
    private String age;
    private String hobbys;
    private String login;
    private int profileImg;

    public ActorVO() {}
    //생성자 자동완성.
    public ActorVO(String name, String age, String hobbys, String login, int profileImg) {
        this.name = name;
        this.age = age;
        this.hobbys = hobbys;
        this.login = login;
        this.profileImg = profileImg;
    }
    //geter/seter는 자동완성 메뉴 삽입.
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getHobbys() {
        return hobbys;
    }

    public void setHobbys(String hobbys) {
        this.hobbys = hobbys;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public int getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(int profileImg) {
        this.profileImg = profileImg;
    }
}
