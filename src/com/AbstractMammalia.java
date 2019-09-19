package com;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public abstract class AbstractMammalia {
    long birthDateTime;
    long deathDateTime;
    int idCard;
    int sex;
    int health = 100;
    AbstractMammalia parent = null;
    AbstractMammalia mother = null;

    /**
     * @param birthDateTime 出生时间
     * @param parent        父
     * @param mother        母
     */
    AbstractMammalia(long birthDateTime, AbstractMammalia parent, AbstractMammalia mother) {
        this.birthDateTime = birthDateTime;
        this.idCard = this.hashCode();
        SecureRandom sr = null;
        try {
            sr = SecureRandom.getInstanceStrong(); // 获取高强度安全随机数生成器
        } catch (NoSuchAlgorithmException e) {
            sr = new SecureRandom(); // 获取普通的安全随机数生成器
        }
//        0:母 1:公
        this.sex = sr.nextInt(2);
        this.parent = parent;
        this.mother = mother;
    }
}
