import javax.servlet.http.{HttpServlet, HttpServletRequest => HSReq, HttpServletResponse => HSResp}
import scala.collection.mutable.ListBuffer
import scala.util.control.Breaks._
import scala.xml._
import java.io.InputStream

class Main extends HttpServlet {
  
  private var phoneBook = new PhoneBook()
  
  println("Main class has been created")
  
  
  override def doPost(req : HSReq, resp : HSResp) = {
    
    req.setCharacterEncoding("utf-8")
    resp.setContentType("text/html;charset=utf-8")
    var action = req.getParameter("action")
    
    if ( action.equals("add") ) {
      processAdd(req, resp)
    } else if ( action.equals("list") ) {
      processShowList(req, resp)
    } else if ( action.equals("del") ) {
      processDelete(req, resp)
    }
    

  }
  
  
  def processAdd(req : HSReq, resp : HSResp) = { 
    
    var name = req.getParameter("name")
    var phone = req.getParameter("phone")
    
    if ( name.isEmpty || phone.isEmpty ) {
      resp.setStatus(418)
      resp.getWriter.print("Необходимо заполнить поля")
    } else if ( phoneBook.getNumber(name, phone) != -1 ) {
      resp.setStatus(500)
      resp.getWriter.print("Данное имя-номер занят")
    } else {
      phoneBook.add(name, phone)
    }
  }
  
  def processShowList(req : HSReq, resp : HSResp) = {
    
    var substring = req.getParameter("sub")
    resp.getWriter.print(phoneBook.showList(substring))
  }
  
  def processDelete(req : HSReq, resp : HSResp) = {
    
    var name = req.getParameter("name")
    var phone = req.getParameter("phone")
    
    
    if ( name.isEmpty || phone.isEmpty ) {
      resp.setStatus(418)
      resp.getWriter.print("Необходимо заполнить поля")
    } else {
      var n = phoneBook.getNumber(name, phone)
      if ( n != -1 ) {
        phoneBook.delete(n, name, phone)
        resp.setStatus(200)
      } else {
        resp.setStatus(500)
        resp.getWriter.print("Данное имя-номер не найдено")
      }
    }
  }
  
}

class PhoneBook() {

  private var list = ListBuffer[PhoneName]()
  
  def add(name : String, phone : String) = {
    list += new PhoneName(name, phone)
    printf("\"%s\" has been added with number: \"%s\"\n", getLast.getName, getLast.getPhone)
  }
  
  def delete(n : Integer, name : String, phone : String) = {
    list.remove(n)
    printf("\"%s\" has been deleted with number: \"%s\"\n", name, phone)
  }
  
  def showList(substring : String) : String = {
    val filteredList = list.filter(p => p.getName.indexOf(substring) != -1)
    <pb>{ filteredList.map(p => p.toXML) }</pb>.toString()  // return xml string
  }
  
  
  def getLast() = list(list.length-1)
  
  def getNumber(name : String, phone : String) : Integer = {
    var i = 0
    var pos = -1
    breakable {
      for ( p <- list ) {
        if ( (p.getName.equals(name)) && (p.getPhone.equals(phone))) {
          pos = i
          break 
        }
        i += 1
      }
    }
    return pos
  }
  
  
  
  
  protected class PhoneName(nameIn : String, phoneIn : String) {
    
    private var name = nameIn
    private var phone = phoneIn
    def getName() = name
    def getPhone() = phone
    def toXML() = <pn><name>{name}</name><phone>{phone}</phone></pn>
  }
  
}

