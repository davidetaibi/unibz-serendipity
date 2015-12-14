package com.unibz.serendipity.utilities;

import com.unibz.serendipity.Sound;

import junit.framework.TestCase;

import java.util.ArrayList;

/**
 * Created by Cody on 29.11.15.
 */
public class SoundListCreatorTest extends TestCase {

    private SoundListCreator testCreator;
    public void testAddSound() throws Exception {
        ArrayList<Sound> exp = new ArrayList<Sound>();
        exp.add(new Sound("testName", 1.0, 2.0, "testLink"));

        testCreator = new SoundListCreator();
        testCreator.addSound("testName", 1.0, 2.0, "testLink");
        ArrayList<Sound> real = testCreator.soundList;

        assertEquals(exp.get(0).equals(real.get(0)),real.get(0).equals(exp.get(0)));


    }
    public void testSoundList() throws Exception{
        assertSame(testCreator.soundList,SoundListCreator.soundList);
    }

}