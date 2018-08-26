package com.udacity.gradle.builditbigger;


import android.support.test.runner.AndroidJUnit4;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class AsyncTest{

    @Test
    public void testHaveJoke() {
        try {
            String joke = null;
            EndpointsAsyncTask jokeTask = new EndpointsAsyncTask();
            jokeTask.execute();
            joke = jokeTask.get(30, TimeUnit.SECONDS);
            Assert.assertNotNull(joke);
        } catch (Exception e){
            fail("Timed out");
        }
    }

}
