package com.teamtreehouse.courses.dao;

import com.teamtreehouse.courses.exc.DaoException;
import com.teamtreehouse.courses.model.Course;
import com.teamtreehouse.courses.model.Review;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

import static org.junit.Assert.*;

public class Sql2oReviewDaoTest {

    private Connection conn;
    private Sql2oCourseDao courseDao;
    private Sql2oReviewDao reviewDao;
    private Course course;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:db/init.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        conn = sql2o.open();
        courseDao = new Sql2oCourseDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);
        course = new Course("Test", "http://test.com");
        courseDao.add(course);
    }

    @After
    public void tearDown() throws Exception {
        conn.close();
    }

    @Test
    public void addingReviewSetsNewId() throws Exception {
        Review review = new Review(course.getId(), 5, "Test comment");
        int originalID = review.getId();

        reviewDao.add(review);

        assertNotEquals(originalID, review.getId());
    }

    @Test
    public void multipleReviewsAreFoundWhenTheyExistForACourse() throws Exception {
        reviewDao.add(new Review(course.getId(), 5, "Test comment 1"));
        reviewDao.add(new Review(course.getId(), 1, "Test comment 2"));

        List<Review> reviews = reviewDao.findByCourseId(course.getId());

        assertEquals(2, reviews.size());
    }

    @Test(expected = DaoException.class)
    public void addingAReviewToANonExistingCourseFails() throws Exception {
        Review review = new Review(42, 5, "Test comment");

        reviewDao.add(review);
    }
}