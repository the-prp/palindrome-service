package com.palindrome;

import javax.ws.rs.Path;
import org.springframework.beans.factory.annotation.Autowired;

@Path("/")
public class LocatingResource {

    @Autowired
    PalindromeRESTService restService;

    @Path("locating")
    public PalindromeRESTService getLocating() {
        return restService;
    }
}
