package com.msports.sportify.server;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;
import com.msports.sportify.shared.DailyStepsEntryOfy;
import com.msports.sportify.shared.Session;

public class OfyService {
    static {
        factory().register(DailyStepsEntryOfy.class);
        factory().register(Session.class);
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