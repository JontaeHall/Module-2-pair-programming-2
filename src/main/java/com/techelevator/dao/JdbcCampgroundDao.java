package com.techelevator.dao;

import com.techelevator.model.Campground;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcCampgroundDao implements CampgroundDao {
    private final String SELECT_CAMPGROUND = "SELECT campground_id, "+
            "park_id, name, open_from_mm, open_to_mm, daily_fee FROM campground ";

    private JdbcTemplate jdbcTemplate;

    public JdbcCampgroundDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Campground getCampgroundById(int id) {
        Campground campground = null;
        String sql = SELECT_CAMPGROUND + "WHERE campground_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
            if(rowSet.next()){
                campground = mapRowToCampground(rowSet);
            }
        return campground;
    }

    @Override
    public List<Campground> getCampgroundsByParkId(int parkId) {
        List<Campground> campgrounds = new ArrayList<>();
        String sql = SELECT_CAMPGROUND + "WHERE park_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, parkId);
        while(rowSet.next()){
            campgrounds.add(mapRowToCampground(rowSet));
        }
        return campgrounds;
    }

    private Campground mapRowToCampground(SqlRowSet results) {
        Campground campground = new Campground();
        campground.setCampgroundId(results.getInt("campground_id"));
        campground.setParkId(results.getInt("park_id"));
        campground.setName(results.getString("name"));
        campground.setOpenFromMonth(results.getInt("open_from_mm"));
        campground.setOpenToMonth(results.getInt("open_to_mm"));
        campground.setDailyFee(results.getDouble("daily_fee"));
        return campground;
    }
}
