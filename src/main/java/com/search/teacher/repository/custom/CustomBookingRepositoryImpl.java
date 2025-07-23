package com.search.teacher.repository.custom;

import com.search.teacher.dto.response.session.BookingResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomBookingRepositoryImpl implements CustomBookingRepository {
    @PersistenceContext
    private EntityManager em;

    @Override
    public Page<BookingResponse> findAllCombinedBookings(LocalDate date, String time, Long branchId, String type, Pageable pageable) {
        String baseSql = "";

        if (type == null || type == "all")
            baseSql = "SELECT b.id, concat(u.firstname, ' ', u.lastname) AS studentName, u.phone, b.status, b.branch_name, b.main_test_date, b.test_time, type, speakerName " +
                    "FROM (" +
                    "   SELECT id, user_id, status, branch_name, main_test_date, test_time, 'TEST' as type, '' as speakerName FROM bookings sb" + filter(date, time, branchId, type) +
                    "   UNION ALL " +
                    "   SELECT id, user_id, status, branch_name, main_test_date, test_time, 'SPEAKING' as type, sb.speaking_full_name as speakerName FROM speaking_bookings sb" + filter(date, time, branchId, type) +
                    ") b " +
                    "JOIN users u ON b.user_id = u.id where u.active ";
        else if (type.equals("full"))
            baseSql = "SELECT sb.id, concat(u.firstname, ' ', u.lastname) AS studentName, u.phone, sb.status, sb.branch_name, sb.main_test_date, sb.test_time, 'TEST' as type, '' as speakerName FROM bookings sb JOIN users u ON sb.user_id = u.id " + filter(date, time, branchId, type);
        else
            baseSql = "   SELECT sb.id, concat(u.firstname, ' ', u.lastname) AS studentName, u.phone, sb.status, sb.branch_name, sb.main_test_date, sb.test_time, 'SPEAKING' as type, sb.speaking_full_name as speakerName FROM speaking_bookings sb JOIN users u ON sb.user_id = u.id " + filter(date, time, branchId, type);

        String countSql = "SELECT COUNT(*) FROM (" + baseSql + ") AS total";

        Query countQuery = em.createNativeQuery(countSql);

        long total = ((Number) countQuery.getSingleResult()).longValue();

        String fullSql = baseSql + " ORDER BY " + (type != null ? " sb.main_test_date " : " b.main_test_date") + " DESC LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();

        Query query = em.createNativeQuery(fullSql);

//        @SuppressWarnings("unchecked")
        List<Object[]> rows = query.getResultList();

        List<BookingResponse> result = rows.stream().map(row ->
                new BookingResponse(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (String) row[2],
                        (String) row[3],
                        (String) row[4],
                        ((java.sql.Date) row[5]).toLocalDate(),
                        (String) row[6],
                        (String) row[7],
                        (String) row[8]
                )
        ).collect(Collectors.toList());

        return new PageImpl<>(result, pageable, total);
    }

    private String filter(LocalDate date, String time, Long branchId, String type) {
        StringBuilder builder = new StringBuilder();
        builder.append(" where sb.active is false ");
        if (date != null) {
            builder.append(" and sb.main_test_date='").append(date).append("' ");
        }

        if (time != null && !"speaking".equals(type)) {
            builder.append(" and sb.test_time='").append(time).append("' ");
        }

        if (branchId != null) {
            builder.append(" and sb.branch_id=").append(branchId).append(" ");
        }

        return builder.toString();
    }
}
