using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.HERE;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class UserController : BaseController
    {
        public UserController() : base(nameof(UserController)) { }

        [HttpGet]
        [Route("api/User/{id}")]
        public BaseResponse RetrieveUser(string id)
        {
            try
            {
                var user = Db.Users.Find(id);
                if (user == null || user.Deleted)
                {
                    throw new Exception("Record not found");
                }

                return CreateOkResponse(user.ToDTO());
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpGet]
        [Route("api/User")]
        public BaseResponse RetrieveUsers()
        {
            try
            {
                var user = Db.Users.Where(x => !x.Deleted).ToList();
                if (user == null)
                {
                    throw new Exception("Record not found");
                }

                return CreateOkResponse(Models.ORM.User.ToListDTO(user));
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpDelete]
        [Route("api/User/{id}")]
        public BaseResponse DeleteUser(string id)
        {
            try
            {
                var user = Db.Users.Find(id);
                if (user == null || user.Deleted)
                {
                    throw new Exception("Record not found");
                }
                user.DateDeleted = DateTime.Now;
                user.Deleted = true;
                Db.SaveChanges();
                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPut]
        [Route("api/User/{id}")]
        public BaseResponse UpdateUser([FromUri] string id, [FromBody] UserDTO userDTO)
        {
            try
            {
                if (id == null || userDTO == null)
                {
                    throw new Exception("Invalid value");
                }

                var user = Db.Users.Find(id);

                if (user == null || user.Deleted)
                {
                    throw new Exception("Record not found");
                }
                if (user.Username != userDTO.Username && Db.Users.Where(x => x.Username == userDTO.Username).Count() > 0)
                {
                    throw new Exception("Username is taken");
                }
                if (user.Email != userDTO.Email && Db.Users.Where(x => x.Email == userDTO.Email).Count() > 0)
                {
                    throw new Exception("Email is taken");
                }
                user.DateModified = DateTime.Now;
                user.Email = userDTO.Email;
                user.DateOfBirth =userDTO.DateOfBirth;
                user.FirstName=userDTO.FirstName;
                user.LastName = userDTO.LastName;
                user.Username = userDTO.Username;

                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

    }
}
