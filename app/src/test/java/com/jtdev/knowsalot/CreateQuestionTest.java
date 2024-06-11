package com.jtdev.knowsalot;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class CreateQuestionTest {
    private Question question;
    private String[] choices;

    @Before
    public void setUp() {
        question = new Question();
        choices = new String[4];
    }

    @Test
    public void testDuplicateQuestion() {
        question.setQuestion("TestExistingQuestion");

    }

    @Test
    public void testCreateQuestion() {
        String[] choices = new String[]{"Choice1", "Choice2", "Choice3", "Choice4"};
        question.setQuestion("TestQuestion");
        question.setChoices(Arrays.asList(choices));


    }


}
