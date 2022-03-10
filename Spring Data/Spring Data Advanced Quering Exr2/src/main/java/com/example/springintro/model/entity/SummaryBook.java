package com.example.springintro.model.entity;

import java.math.BigDecimal;

public interface SummaryBook {

    String getTitle();
    String getEditionType();
    String getAgeRestriction();
    BigDecimal getPrice();
}
