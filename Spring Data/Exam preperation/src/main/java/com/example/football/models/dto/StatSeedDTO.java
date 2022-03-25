package com.example.football.models.dto;

import javax.validation.constraints.Positive;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class StatSeedDTO {

    @XmlElement(name = "passing")
    @Positive
    private float passing;

    @XmlElement(name = "shooting")
    @Positive
    private float shooting;

    @XmlElement(name = "endurance")
    @Positive
    private float endurance;


    public StatSeedDTO() {
    }

    public float getPassing() {
        return passing;
    }

    public float getShooting() {
        return shooting;
    }

    public float getEndurance() {
        return endurance;
    }
}
