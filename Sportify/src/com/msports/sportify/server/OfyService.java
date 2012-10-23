package com.msports.sportify.server;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.msports.sportify.shared.DailyStepsEntryOfy;

public class OfyService {
    static {
        factory().register(DailyStepsEntryOfy.class);
//        factory().register(OtherThing.class);
//        ...etc
    }

    public static Objectify ofy() {
        return ObjectifyService.begin();
    }

    public static ObjectifyFactory factory() {
        return ObjectifyService.factory();
    }
}