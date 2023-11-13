package com.techelevator.dao;

import com.techelevator.model.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;

public class JdbcReservationDao implements ReservationDao {

    private  final String SELECT_RESERVATION = "SELECT DISTINCT reservation_id, "+
            "site_id, name, from_date, to_date, create_date FROM reservation ";
    private JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override 
    public Reservation getReservationById(int id) {
        Reservation reservation = null;
        String sql = SELECT_RESERVATION + "WHERE reservation_id = ?";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, id);
        if(rowSet.next()){
            reservation = mapRowToReservation(rowSet);
        }
        return reservation;
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        String sql = "INSERT  INTO reservation (reservation_id, site_id, name, from_date, to_date, create_date) "+
                "VALUES (?, ?, ?, ?, ?, ?) RETURNING reservation_id;";
        int reservationID = jdbcTemplate.queryForObject(sql,int.class, reservation.getReservationId(),reservation.getSiteId(), reservation.getName(), reservation.getFromDate(), reservation.getToDate(), reservation.getCreateDate());

        return getReservationById(reservationID);
    }

    private Reservation mapRowToReservation(SqlRowSet results) {
        Reservation reservation = new Reservation();
        reservation.setReservationId(results.getInt("reservation_id"));
        reservation.setSiteId(results.getInt("site_id"));
        reservation.setName(results.getString("name"));
        reservation.setFromDate(results.getDate("from_date").toLocalDate());
        reservation.setToDate(results.getDate("to_date").toLocalDate());
        reservation.setCreateDate(results.getDate("create_date").toLocalDate());
        return reservation;
    }


}
