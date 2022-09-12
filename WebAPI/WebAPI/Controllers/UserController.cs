using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.Exceptions;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class UserController : BaseController
    {
        public UserController() : base(nameof(UserController)) { }

        [HttpGet]
        [Route("api/User/{id}?expanded={expanded:bool=false}")]
        public BaseResponse RetrieveUser(string id, bool expanded)
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
        [Route("api/User/{id}/Request/Active")]
        public BaseResponse RetrieveActiveUserRequest(string id)
        {
            try
            {
                var user = Db.Users.Find(id);
                if (user == null || user.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var activeRequest = user.Requests.Where(x => !x.Completed && !x.Deleted)?.OrderBy(x => x.DateCreated)?.FirstOrDefault();
                if (activeRequest == null || activeRequest.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var activeRequestDTO = activeRequest.ToDTO(false);
                activeRequestDTO.Shop.Location = activeRequest.Shop.Location.ToDTO();

                return CreateOkResponse(activeRequestDTO);
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
        [Route("api/User/{id}/Request")]
        public BaseResponse RetrieveUserRequests(string id)
        {
            try
            {
                var user = Db.Users.Find(id);
                if (user == null || user.Deleted)
                {
                    throw new RecordNotFoundException();
                }

                var activeRequests = user.Requests.Where(x => !x.Completed && !x.Deleted)?.OrderBy(x => x.DateCreated)?.ToList();
                if (activeRequests == null)
                {
                    throw new RecordNotFoundException();
                }

                return CreateOkResponse(Models.ORM.Request.ToListDTO(activeRequests));
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
        public BaseResponse DeleteUser(string id)
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
