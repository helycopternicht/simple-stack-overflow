package com.elazarev.repository;

import com.elazarev.domain.Complaint;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for complaints table.
 * @author Eugene Lazarev mailto(helycopternicht@rambler.ru)
 * @since 14.02.18
 */
@Repository
public interface ComplaintRepository extends PagingAndSortingRepository<Complaint, Long> {
}
