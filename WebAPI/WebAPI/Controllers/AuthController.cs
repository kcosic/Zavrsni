using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using WebAPI.Auth;
using WebAPI.Models;
using WebAPI.Models.DTOs;
using WebAPI.Models.Enums;
using WebAPI.Models.Helpers;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Controllers
{
    public class AuthController : BaseController
    {
        public AuthController() : base(nameof(AuthController)) { }

        public BaseResponse Get()
        {
            try
            {
                return CreateOkResponse(AuthUser.Token.ToDTO(false));
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, Models.Enums.ErrorCodeEnum.UnexpectedError);
            }
        }

        [AllowAnonymous]
        public BaseResponse Post([FromBody]UserDTO user)
        {
            try
            {
                if(user == null)
                {
                    throw new ArgumentNullException("User cannot be null");
                }

                var existingUser = Db.Users.Where(x => x.Username == user.Username).FirstOrDefault();
                if(existingUser != null)
                {
                    return CreateErrorResponse("Username already exists.", ErrorCodeEnum.UsernameExists);
                }       
                
                existingUser = Db.Users.Where(x => x.Email == user.Email).FirstOrDefault();
                if(existingUser != null)
                {
                    return CreateErrorResponse("Email already exists.", ErrorCodeEnum.EmailExists);
                }

                var newUser = new User
                {
                    DateCreated = DateTime.Now.ToUniversalTime(),
                    DateModified = DateTime.Now.ToUniversalTime(),
                    DateOfBirth = new DateTime(user.DateOfBirth.Year, user.DateOfBirth.Month + 1, user.DateOfBirth.Day),
                    Email = user.Email,
                    FirstName = user.FirstName,
                    LastName = user.LastName,
                    Password = Helper.Hash(user.Password),
                    Username = user.Username,
                    Deleted = false
                   
                };

                Db.Users.Add(newUser);
                Db.SaveChanges();

                return CreateOkResponse();                
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, ErrorCodeEnum.UnexpectedError);
            }
        }

        //// PUT: api/Auth/5
        //public void Put(int id, [FromBody]string value)
        //{
        //}

        //// DELETE: api/Auth/5
        //public void Delete(int id)
        //{
        //}
    }
}
