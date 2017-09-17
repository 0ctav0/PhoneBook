import javax.servlet.http.{HttpServlet,
  HttpServletRequest => HSReq, HttpServletResponse => HSResp}

class Main extends HttpServlet
{
  def message =
    <html>
      <head><title>Hello, Scala!</title></head>
      <body>Привет, { name } Сейчас { currentDate }</body>
    </html>
  def currentDate = java.util.Calendar.getInstance().getTime()
  var name = "";
  
  println("I has been created.")

  override def doGet(req : HSReq, resp : HSResp) = {
    resp setContentType "text/html;charset=utf-8"
    name = req getParameter "username" toUpperCase()
    resp.getWriter print message
  }
}