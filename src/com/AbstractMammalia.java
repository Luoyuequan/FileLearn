package com;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 生物抽象类
 */
abstract class AbstractOrganism implements Serializable {
    final long birthDateTime;
    long deathDateTime;
    int health;
    private final int idCard;

    /**
     * @param birthDateTime 出生日期
     */
    AbstractOrganism(long birthDateTime) {
        this.birthDateTime = birthDateTime;
        this.idCard = this.hashCode();
        health = 100;
    }

    /**
     * 繁殖
     */
    abstract void breeding();

    /**
     * 运动
     */
    abstract void movement();

    /**
     * @return 唯一标识符 DNA
     */
    public int getIdCard() {
        return idCard;
    }
}

/**
 * 哺乳动物抽象类
 *
 * @param <T> 子类
 */
abstract class AbstractMammalia<T> extends AbstractOrganism {
    //            0:母 1:公
    int sex;
    T father;
    T mother;
    //    配偶
    T spouse;
    //    子代
    List<T> children = new ArrayList<>();

    /**
     * @param birthDateTime 出生日期 用于父类 构造函数
     * @param father        父
     * @param mother        母
     */
    AbstractMammalia(long birthDateTime, T father, T mother) {
        super(birthDateTime);
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstanceStrong(); // 获取高强度安全随机数生成器
        } catch (NoSuchAlgorithmException e) {
            sr = new SecureRandom(); // 获取普通的安全随机数生成器
        }
        this.sex = sr.nextInt(2);
        this.father = father;
        this.mother = mother;
    }

    /**
     * 觅食
     */
    abstract void foraging();
}

/**
 * @implNote 继承 哺乳动物抽象类 并实现
 */
class Person extends AbstractMammalia<Person> {
    //    姓
    String surname;
    //    名
    String name;
    int age;

    /**
     * @param birthDateTime 出生日期
     * @param father        父亲
     * @param mother        母亲
     */
    Person(long birthDateTime, Person father, Person mother, String name) {
        super(birthDateTime, father, mother);
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstanceStrong(); // 获取高强度安全随机数生成器
        } catch (NoSuchAlgorithmException e) {
            sr = new SecureRandom(); // 获取普通的安全随机数生成器
        }
        this.surname =
                father == null && mother == null ?
                        "Yuna" + sr.nextInt(10) + "·" :
                        father != null ? father.surname : mother.surname;
        this.name = name;
        this.getPersonInfor();
    }

    /**
     * 觅食
     */
    @Override
    void foraging() {

    }

    /**
     * 繁殖
     */
    @Override
    void breeding() {
        if (this.spouse != null) {
            Person father = this.sex == 1 ? this : this.spouse;
            Person mother = this.sex == 0 ? this : this.spouse;
            Person child = new Person(System.currentTimeMillis(), father, mother, "Child" + this.children.size());
            this.children.add(child);
            this.spouse.children.add(child);
            System.out.printf("%s与%s繁殖成功,子代为%s\n", this.getSurnameName(), this.spouse.getSurnameName(), child.getSurnameName());
            this.getPersonInfor();
            this.spouse.getPersonInfor();
        } else {
            System.out.println(this.getSurnameName() + "暂无配偶,无法繁殖后代!");
        }
    }

    /**
     * 运动
     */
    @Override
    void movement() {

    }

    /**
     * 结婚
     *
     * @param spouse 配偶
     */
    void marriage(Person spouse) {
        if (spouse == null) {
            System.out.println("请传入有效对象!");
        } else if (spouse.sex + this.sex == 1) {
            this.spouse = spouse;
            spouse.spouse = this;
            System.out.printf("%s与%s结婚成功!\n", this.getSurnameName(), spouse.getSurnameName());

            this.getPersonInfor();
            this.spouse.getPersonInfor();
        } else if (spouse.sex + this.sex != 1) {
            System.out.println(this.getSurnameName() + "结婚失败,配偶必须为异性!");
        }
    }

    /**
     * 讲话
     */
    void talking() {

    }

    /**
     * 听话
     */
    void listen() {

    }

    /**
     * 获取个人基本信息
     */
    void getPersonInfor() {
//        System.out.println(this.birthDateTime);
        var sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss SSS");
        System.out.printf(
                "姓名: %s,出生日期:%s,死亡日期:%s,性别:%s,健康值:%d,父亲:%s,母亲:%s,配偶:%s,子代:%d个\n",
                this.getSurnameName(),
                sdf.format(this.birthDateTime),
                sdf.format(this.deathDateTime),
                this.getSex(),
                this.health,
                this.father == null ? "无" : this.father.getSurnameName(),
                this.mother == null ? "无" : this.mother.getSurnameName(),
                this.spouse == null ? "无" : this.spouse.getSurnameName(),
                this.children.size()
        );
    }

    /**
     * @return 姓名
     */
    String getSurnameName() {
        return this.surname + this.name;
    }

    /**
     * @return 性别
     */
    String getSex() {
        return this.sex == 0 ? "女♀" : "男♂";
    }

    /**
     * 获取子代信息
     */
    void getChildren() {
        for (Person child : this.children) {
            child.getPersonInfor();
        }
    }
}

class Main implements Runnable {
    public static void main(String[] args) {

//        Main my = new Main();
//        new Thread(my).start();
        Person person = new Person(System.currentTimeMillis(), null, null, "One");
        Person person1 = new Person(System.currentTimeMillis(), null, null, "Two");

        try {
            FileOutputStream fileOut = new FileOutputStream("./" + person.getIdCard() + ".ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(person);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
        Person e = null;
        try {
            FileInputStream fileIn = new FileInputStream("./" + person.getIdCard() + ".ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            e = (Person) in.readObject();
            in.close();
            fileIn.close();
            e.getPersonInfor();
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Person class not found");
            c.printStackTrace();
        }
    }

    @Override
    public void run() {
//        Person person = new Person(System.currentTimeMillis(), null, null, "One");
//        Person person1 = new Person(System.currentTimeMillis(), null, null, "Two");
    }
}
