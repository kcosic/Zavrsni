using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class AuthController : BaseController
    {
        public AuthController() : base(nameof(AuthController)) { }

        [HttpGet]
        [Route("api/Auth/Login")]
        public BaseResponse Login()
        {
            try
            {
                Log(Models.Enums.SeverityEnum.Error, "Greška jbte");
                return CreateOkResponse(AuthUser.Token.TokenValue);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [AllowAnonymous]
        [HttpPost]
        [Route("api/Auth/Register")]
        public string Register([FromBody] User user)
        {
            return "value";
        }

        // POST: api/Auth
        public void Post([FromBody]string value)
        {
        }

        // PUT: api/Auth/5
        public void Put(int id, [FromBody]string value)
        {
        }

        // DELETE: api/Auth/5
        public void Delete(int id)
        {
        }
    }
}
