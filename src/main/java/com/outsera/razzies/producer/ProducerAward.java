package com.outsera.razzies.producer;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder
@EqualsAndHashCode
public class ProducerAward  implements Serializable {

    private String producer;
    private long interval;
    private Integer previousWin;
    private Integer followingWin;
}