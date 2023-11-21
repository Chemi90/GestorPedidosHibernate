package com.example.gestionpedidoscondao.domain.pedido;

import com.example.gestionpedidoscondao.domain.DAO;
import com.example.gestionpedidoscondao.domain.HibernateUtil;
import com.example.gestionpedidoscondao.domain.itemPedido.ItemPedido;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase PedidoDAO.
 * Implementa la interfaz DAO para la gestión de objetos de tipo Pedido en la base de datos.
 * Permite realizar operaciones como buscar, guardar, actualizar y eliminar pedidos.
 *
 * @author Author Name
 * @version 1.0
 * @since 2023-11-21
 */
public class PedidoDAO implements DAO<Pedido> {

    /**
     * Encuentra y devuelve una lista de pedidos asociados a un ID de usuario específico.
     * Utiliza una consulta HQL para recuperar los pedidos desde la base de datos.
     * Maneja excepciones internamente y devuelve una lista vacía en caso de error.
     *
     * @param usuarioId El ID del usuario para buscar los pedidos asociados.
     * @return List<Pedido> Lista de pedidos asociados al usuario especificado.
     */
    public List<Pedido> findByUsuarioId(int usuarioId) {
        List<Pedido> pedidos = new ArrayList<>();
        try (org.hibernate.Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Pedido> query = session.createQuery(
                    "from Pedido where usuario.id = :usuarioId", Pedido.class);
            query.setParameter("usuarioId", usuarioId);
            pedidos = query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pedidos;
    }

    /**
     * Método no implementado para obtener todos los pedidos.
     *
     * @return ArrayList<Pedido> Retorna null ya que el método no está implementado.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public ArrayList<Pedido> getAll() {return null;}

    /**
     * Método no implementado para obtener un Pedido por su ID.
     *
     * @param id El ID único del Pedido a obtener.
     * @return Pedido Retorna null ya que el método no está implementado.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public Pedido get(Long id) {return null;}

    /**
     * Guarda un objeto Pedido en la base de datos.
     * Abre una sesión con Hibernate, inicia una transacción, guarda el pedido,
     * y maneja las transacciones y excepciones correspondientes.
     *
     * @param data El objeto Pedido a ser guardado.
     * @return Pedido El objeto Pedido guardado, o null en caso de error.
     */
    @Override
    public Pedido save(Pedido data) {
        Pedido salida = null;
        try( org.hibernate.Session s = HibernateUtil.getSessionFactory().openSession()){
            Transaction t = s.beginTransaction();
            s.persist(data);
            t.commit();
            salida = data;
        }catch (Exception e){
            e.printStackTrace();
        }
        return salida;
    }

    /**
     * Encuentra un pedido por su código.
     * Inicializa la colección de ítems asociados al pedido y recalcula el total del pedido.
     *
     * @param codigo El código del pedido a buscar.
     * @return Pedido El objeto Pedido encontrado, o null si no se encuentra o si ocurre un error.
     */
    public Pedido findByCodigo(String codigo) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Pedido pedido = session.createQuery("FROM Pedido WHERE codigo = :codigo", Pedido.class)
                    .setParameter("codigo", codigo)
                    .uniqueResult();
            Hibernate.initialize(pedido.getItemsPedidos()); // Inicializar la colección

            // Verificar y calcular el total
            double total = 0.0;
            for (ItemPedido item : pedido.getItemsPedidos()) {
                total += item.getPrecioTotal(); // Asegúrate de que getPrecioTotal() está calculando correctamente
            }
            pedido.setTotal(total);

            return pedido;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Método no implementado para actualizar un Pedido.
     *
     * @param data El objeto Pedido a actualizar.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public void update(Pedido data) {}

    /**
     * Método no implementado para eliminar un Pedido.
     *
     * @param data El objeto Pedido a eliminar.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public void delete(Pedido data) {}

    /**
     * Método no implementado para eliminar un Pedido.
     *
     * @param pedido El objeto Pedido a eliminar.
     * @return boolean Siempre retorna falso ya que el método no está implementado.
     * @deprecated Este método no está implementado. Su uso no es recomendado.
     */
    @Override
    public boolean remove(Pedido pedido) {
        return false;
    }

    /**
     * Elimina un pedido y sus ítems asociados de la base de datos según el código del pedido.
     * Realiza la operación en una transacción y maneja posibles errores.
     *
     * @param codigoPedido El código del pedido a eliminar.
     */
    public void deleteByCodigo(String codigoPedido) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            Query itemPedidoDeleteQuery = session.createQuery("DELETE FROM ItemPedido ip WHERE ip.pedido.codigo = :codigoPedido");
            itemPedidoDeleteQuery.setParameter("codigoPedido", codigoPedido);
            itemPedidoDeleteQuery.executeUpdate();

            Query<Pedido> pedidoQuery = session.createQuery("FROM Pedido WHERE codigo = :codigo", Pedido.class);
            pedidoQuery.setParameter("codigo", codigoPedido);
            Pedido pedido = pedidoQuery.uniqueResult();
            if (pedido != null) {
                session.remove(pedido);
            }

            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
