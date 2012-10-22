package com.msports.sportify.server;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyFactory;
import com.googlecode.objectify.ObjectifyService;

public class OfyService {
    static {
//        factory().register(Thing.class);
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