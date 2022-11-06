using System;
using System.Linq;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.Exceptions;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class UserController : BaseController
    {
        public UserController() : base(nameof(UserController)) { }

        [HttpGet]
        [Route("api/User/{id}")]
        public BaseResponse RetrieveUser(int id, bool expanded = false)
        {
            try
            {
                var user = Db.Users.Find(id);
                if (user == null || user.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(user.ToDTO(!expanded));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }            
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpGet]
        [Route("api/User/Requests")]
        public BaseResponse RetrieveUserRequests()
        {
            try
            {
                var requests = Db.Requests.Where(request=> request.UserId == AuthUser.Id && !request.Deleted).ToList();
                if (requests == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Models.ORM.Request.ToListDTO(requests, false));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
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
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Models.ORM.User.ToListDTO(user));
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }

        }

        [HttpDelete]
        [Route("api/User/{id}")]
        public BaseResponse DeleteUser(int id)
        {
            try
            {
                var user = Db.Users.Find(id);
                if (user == null || user.Deleted)
                {
                    throw new RecordNotFoundException();
                }
                user.DateDeleted = DateTime.Now;
                user.Deleted = true;
                Db.SaveChanges();
                return CreateOkResponse();
            }
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [HttpPut]
        [Route("api/User/{id}")]
        public BaseResponse UpdateUser([FromUri] int id, [FromBody] UserDTO userDTO)
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
                    throw new RecordNotFoundException();
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
            catch (RecordNotFoundException e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.RecordNotFound);
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

    }
}
