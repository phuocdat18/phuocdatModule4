package cg.repository;

import cg.model.Transfer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;
@Repository
@Transactional
public class TransferRepository implements ITransferRepository{
    @PersistenceContext
    private EntityManager em;
    @Override
    public List<Transfer> findAll() {
        String sql = "select t from Transfer t";
        TypedQuery<Transfer> query = em.createQuery(sql, Transfer.class);
        return query.getResultList();
    }

    @Override
    public Transfer findById(Long id) {
        String sql = "select t from Transfer t where  t.id=:id";
        TypedQuery<Transfer> query = em.createQuery(sql, Transfer.class);
        query.setParameter("id", id);
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Transfer> findRecipients(Long id) {
        return null;
    }

    @Override
    public void save(Transfer transfer) {
        if (transfer != null) {
            em.merge(transfer);
        } else {
            em.persist(transfer);
        }
    }

    @Override
    public void remove(Long id) {
        Transfer transfer = findById(id);
        if (transfer != null) {
            em.remove(transfer);
        }
    }
}
