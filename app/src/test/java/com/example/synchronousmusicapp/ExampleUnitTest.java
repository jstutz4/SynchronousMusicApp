package com.example.synchronousmusicapp;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }


    @Test
    public void checkGroupId(){
        JoinGroup groupId = new JoinGroup();
        assertEquals(true, groupId.checkGroupId("testID101"));
    }

    @Test
    public void isPlayingMusic(){
        PlayMusic state = new PlayMusic();
        assertEquals(false, state.musicState());
    }

    @Test
    public void linkState(){
        assertEquals(false,true);
    }
}