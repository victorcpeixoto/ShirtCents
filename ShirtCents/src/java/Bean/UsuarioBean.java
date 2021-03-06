package Bean;

import Entidades.Usuario;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import util.JpaUtil;
//Pedro Viado
//Commit Pedro
//Victor Cesar

//Pablo na area muleque
@ManagedBean
@SessionScoped
public class UsuarioBean {
    
    private Usuario usuario = new Usuario();
    private List<Usuario> usuarios = new ArrayList<>();
    
    public UsuarioBean() {
        listarTodos();
    }
    
    public void modificaOpcaoRadio(){
        if(usuario.getProfile().equals(String.valueOf(0))){
            usuario.setProfile("Admin");
        }else usuario.setProfile("Leitor");
        
    }
    
    
   public void salvar() {
        modificaOpcaoRadio();
        salvarCliente();
        listarTodos();
        novo();
    }
   
   public void novo(){ 
        usuario = new Usuario();
    }
   
   public void apagar(){
       apagarUsuario();
       listarTodos();
       novo();
   }
   
   
   
   private void salvarCliente() {
       
        EntityManager manager = null;
        EntityTransaction etx = null;
        try {
            manager = JpaUtil.getEntityManager(); //equivale a uma conexão
            etx = manager.getTransaction();
            etx.begin();

            usuario = manager.merge(usuario);     //insert ou update

            etx.commit();
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK! ", "Cliente salvo com sucesso! " + new java.util.Date()));
        } catch (Exception ex) {
            try {
                etx.rollback();
            } catch (Exception ex2) { /* nada aui por hora */            }
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: ", ex.getMessage()));
        } finally {
            JpaUtil.closeEntityManager(manager);
        }
    }
    
   private void apagarUsuario() {
       
        EntityManager manager = null;
        EntityTransaction etx = null;
        try {
            manager = JpaUtil.getEntityManager(); //equivale a uma conexão
            etx = manager.getTransaction();
            etx.begin();

            usuario = manager.find(Usuario.class, usuario.getId());
            manager.remove(usuario);

            etx.commit();
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "OK! ", "Usuario apagado com sucesso! " + new java.util.Date()));
        } catch (Exception ex) {
            try {
                etx.rollback();
            } catch (Exception ex2) { /* nada aui por hora */            }
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: ", ex.getMessage()));
        } finally {
            JpaUtil.closeEntityManager(manager);
        }
    }
    
    
    private void listarTodos() {
        EntityManager manager = null;
        try {
            manager = JpaUtil.getEntityManager(); //equivale a uma conexão

            usuarios = manager
                    .createQuery("Select usua from Usuario usua order by usua.id", Usuario.class)
                    .getResultList();

        } catch (Exception ex) {
            FacesContext.getCurrentInstance()
                    .addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro: ", ex.getMessage()));
        } finally {
            JpaUtil.closeEntityManager(manager);
        }
        
        
    }
    

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public List<Usuario> getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }
    
}
