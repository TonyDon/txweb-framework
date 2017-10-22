/*
 * @(#)DemoEntity.java 2013-6-16
 * 
 * Copy Right@ uuola
 */ 

package com.uuola.txweb.test;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



/**
 * <pre>
 *
 * @author tangxiaodong
 * 创建日期: 2013-6-16
 * </pre>
 */
@Entity
@Table(name="USER_INFO")
public class DemoEntity{

    @Id
    private Long id;
    
    @Column(name="CODE")
    private String code;
    
    @Column(name ="NICK")
    private String nick;
    
    @Column(name ="NAME")
    private String name;
    
    @Column(name ="EMAIL")
    private String email;
    
    @Column(name ="TEL")
    private String tel;
    
    @Column(name ="PASSKEY")
    private String passkey;
    
    @Column(name ="CITYCODE")
    private String citycode;
    
    @Column(name ="REGTIME")
    private Long regtime;
    
    @Column(name ="ICONURL")
    private String iconurl;
    
    @Column(name ="GENDER")
    private Byte gender;
    
    @Column(name ="BIRTHDAY")
    private Date birthday;
    
    @Column(name ="WEDSTATUS")
    private Byte wedstatus;
    
    @Column(name ="DEGREESTATUS")
    private Integer degreestatus;
    
    @Column(name ="VIPCARD")
    private String vipcard;

    
    public Long getId() {
        return id;
    }

    
    public void setId(Long id) {
        this.id = id;
    }

    
    public String getCode() {
        return code;
    }

    
    public void setCode(String code) {
        this.code = code;
    }

    
    public String getNick() {
        return nick;
    }

    
    public void setNick(String nick) {
        this.nick = nick;
    }

    
    public String getName() {
        return name;
    }

    
    public void setName(String name) {
        this.name = name;
    }

    
    public String getEmail() {
        return email;
    }

    
    public void setEmail(String email) {
        this.email = email;
    }

    
    public String getTel() {
        return tel;
    }

    
    public void setTel(String tel) {
        this.tel = tel;
    }

    
    public String getPasskey() {
        return passkey;
    }

    
    public void setPasskey(String passkey) {
        this.passkey = passkey;
    }

    
    public String getCitycode() {
        return citycode;
    }

    
    public void setCitycode(String citycode) {
        this.citycode = citycode;
    }

    
    public Long getRegtime() {
        return regtime;
    }

    
    public void setRegtime(Long regtime) {
        this.regtime = regtime;
    }

    
    public String getIconurl() {
        return iconurl;
    }

    
    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    
    public Byte getGender() {
        return gender;
    }

    
    public void setGender(Byte gender) {
        this.gender = gender;
    }

    
    public Date getBirthday() {
        return birthday;
    }

    
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    
    public Byte getWedstatus() {
        return wedstatus;
    }

    
    public void setWedstatus(Byte wedstatus) {
        this.wedstatus = wedstatus;
    }

    
    public Integer getDegreestatus() {
        return degreestatus;
    }

    
    public void setDegreestatus(Integer degreestatus) {
        this.degreestatus = degreestatus;
    }

    
    public String getVipcard() {
        return vipcard;
    }

    
    public void setVipcard(String vipcard) {
        this.vipcard = vipcard;
    }
}
