import javax.servlet.http.{HttpServlet, HttpServletRequest => HSReq, HttpServletResponse => HSResp}
import scala.collection.mutable.MutableList
import javax.servlet.AsyncContext
import javax.servlet.ServletContext

class Main extends HttpServlet {
  
  private var name = ""
  private var phone = ""
  private var phoneBook = new PhoneBook()
  
  println("Main class has been created")
  
  
  override def doPost(req : HSReq, resp : HSResp) = {
    req.setCharacterEncoding("utf-8")
    name = req.getParameter("pb_name")
    phone = req.getParameter("pb_phone")
    resp.setContentType("text/html;charset=utf-8")
    if ( name.isEmpty || phone.isEmpty ) {
      resp.setStatus(418)
      resp.getWriter.print("Необходимо заполнить поля")
    } else if ( phoneBook.isHas(name, phone) ) {
      resp.setStatus(500)
      resp.getWriter.print("Данное имя-номер занят")
    } else {
      phoneBook.add(name, phone)
    }
    
    
    //
    //resp.setStatus(200);
    //resp.getWriter.print("Запись успешно добавлена")
  }
}

class PhoneBook() {

  private var list = MutableList[PhoneName]()
  
  def add(name : String, phone : String) = {
    list += new PhoneName(name, phone)
    printf("\"%s\" has been added with number: \"%s\"\n", getLast.getName, getLast.getPhone)
  }
  
  def getLast() = list(list.length-1)
  
  def isHas(name : String, phone : String) : Boolean = {
//    var e = new PhoneName(name, phone);
//    var b = false;
//    for ( e <- list ) {
//      if ( e.getName.equalsIgnoreCase(e.getName) && e.getPhone.equalsIgnoreCase(e.getPhone) ) {
//        return true
//      }
//    }
    //var elements = new MutableList[PhoneName]()
    return list.exists( p => (p.getName.equalsIgnoreCase(name)) && (p.getPhone.equalsIgnoreCase(phone)) )
  }
  
  protected class PhoneName(nameIn : String, phoneIn : String) {
    
    private var name = nameIn
    private var phone = phoneIn
    def getName() = name
    def getPhone() = phone
  }
}

