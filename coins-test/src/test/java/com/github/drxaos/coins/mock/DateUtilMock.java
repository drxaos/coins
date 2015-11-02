package com.github.drxaos.coins.mock;

import com.github.drxaos.coins.utils.DateUtil;

import java.util.Date;

public class DateUtilMock extends DateUtil {

    Date mockDate = new Date();

    public void setMockDate(Date mockDate) {
        this.mockDate = mockDate;
    }

    @Override
    public Date now() {
        return mockDate;
    }
}
