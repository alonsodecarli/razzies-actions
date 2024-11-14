package com.outsera.razzies.movie.enumeration;

public enum Winner {
    YES, NO;

    public static Winner fromString(String value) {
        if (value != null) {
            for (Winner winner : Winner.values()) {
                if (winner.name().equalsIgnoreCase(value.toUpperCase())) {
                    return winner;
                }
            }
        }
        return NO;
    }

}
