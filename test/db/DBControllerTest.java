package db;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DBControllerTest {
    Bike testBike;
    User testUser;

    @Before
    public void setUp() throws Exception {
        testBike = new Bike();
        testUser = new User("test", "testPW");

        DBController.getInstance().insertBike(testBike);
        DBController.getInstance().insertUser(testUser);
    }

    @After
    public void tearDown() throws Exception {
        DBController.getInstance().removeBike(testBike);
        DBController.getInstance().removeUser(testUser);
        clearAll();
    }

    @Test
    public void clearAll() {
        DBController.getInstance().clearBikes();
        DBController.getInstance().clearUsers();
    }

    @Test
    public void insertBike() throws Exception {
        Assert.assertTrue(DBController.getInstance().insertBike(testBike));
    }

    @Test
    public void insertUser() throws Exception {
        Assert.assertTrue(DBController.getInstance().insertUser(testUser));
    }

    @Test
    public void getBikeById() throws Exception {
        Bike retrievedBike = DBController.getInstance().getBikeById(testBike.getId());
        Assert.assertEquals(retrievedBike.getId().hashCode(), testBike.getId().hashCode());
    }

    @Test
    public void getUserById() throws Exception {
        User retrievedUser = DBController.getInstance().getUserById(testUser.getId());
        Assert.assertEquals(retrievedUser.getId().hashCode(), testUser.getId().hashCode());
    }

    @Test
    public void updateBike() throws Exception {
        Boolean oldValue = testBike.getAvailable();
        testBike.setAvailable(!testBike.getAvailable());
        DBController.getInstance().updateBike(testBike);
        Bike retrievedBike = DBController.getInstance().getBikeById(testBike.getId());
        Assert.assertNotSame(retrievedBike.getAvailable(), oldValue);
    }

    @Test
    public void getBikes() throws Exception {
        Assert.assertNotNull(DBController.getInstance().getBikes());
    }

    @Test
    public void getUsers() throws Exception {
        Assert.assertNotNull(DBController.getInstance().getUsers());
    }

    @Test
    public void removeBike() throws Exception {
        Assert.assertTrue(DBController.getInstance().removeBike(testBike));
    }

    @Test
    public void removeUser() throws Exception {
        Assert.assertTrue(DBController.getInstance().removeUser(testUser));
    }





}