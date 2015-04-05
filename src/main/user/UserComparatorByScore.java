package main.user;

import main.user.UserProfile;

import java.util.Comparator;

/**
 * Created by neikila on 31.03.15.
 */
public class UserComparatorByScore implements Comparator<UserProfile> {

    @Override
    public int compare(UserProfile o1, UserProfile o2) {
        return o2.getScore() - o1.getScore();
    }
}
