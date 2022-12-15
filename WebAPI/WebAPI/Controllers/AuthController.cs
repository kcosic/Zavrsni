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
    [Route("api/Auth")]
    public class AuthController : BaseController
    {
        public AuthController() : base(nameof(AuthController)) { }


        [HttpGet]
        [Route("Login")]
        public BaseResponse Login()
        {
            try
            {
                if (AuthUser != null)
                {
                    return CreateOkResponse(AuthUser.Tokens.First().ToDTO(2));
                }
                else if(AuthShop != null)
                {
                    return CreateOkResponse(AuthShop.Tokens.First().ToDTO(2));
                } 
                else
                {
                    return CreateErrorResponse("Something happened", ErrorCodeEnum.UnexpectedError);
                }
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, ErrorCodeEnum.UnexpectedError);
            }
        }

        [AllowAnonymous]
        [HttpPost]
        [Route("Register/User")]
        public BaseResponse RegisterUser([FromBody] UserDTO user)
        {
            try
            {
                if (user == null)
                {
                    throw new ArgumentNullException("User cannot be null");
                }

                var existingUser = Db.Users.Where(x => x.Username == user.Username).FirstOrDefault();
                if (existingUser != null)
                {
                    return CreateErrorResponse("Username already exists.", ErrorCodeEnum.UsernameExists);
                }

                existingUser = Db.Users.Where(x => x.Email == user.Email).FirstOrDefault();
                if (existingUser != null)
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

        [AllowAnonymous]
        [HttpPost]
        [Route("Register/Shop")]
        public BaseResponse RegisterShop([FromBody] ShopDTO shop)
        {
            try
            {
                if (shop == null)
                {
                    throw new ArgumentNullException("Shop cannot be null");
                }

                var existingShop = Db.Shops.Where(x => x.Vat == shop.Vat).FirstOrDefault();
                if (existingShop != null)
                {
                    return CreateErrorResponse("VAT already exists.", ErrorCodeEnum.VatExists);
                }
                existingShop = Db.Shops.Where(x => x.LegalName == shop.LegalName).FirstOrDefault();
                if (existingShop != null)
                {
                    return CreateErrorResponse("Legal name already exists.", ErrorCodeEnum.UsernameExists);
                }

                existingShop = Db.Shops.Where(x => x.ShortName == shop.ShortName).FirstOrDefault();
                if (existingShop != null)
                {
                    return CreateErrorResponse("Short name already exists.", ErrorCodeEnum.UsernameExists);
                }

                existingShop = Db.Shops.Where(x => x.Email == shop.Email).FirstOrDefault();
                if (existingShop != null)
                {
                    return CreateErrorResponse("Email already exists.", ErrorCodeEnum.EmailExists);
                }

                var newShopLocation = new Location
                {
                    DateCreated = DateTime.Now.ToUniversalTime(),
                    DateModified = DateTime.Now.ToUniversalTime(),
                    Deleted = false,
                    Latitude = shop.Location.Latitude,
                    City = shop.Location.City,
                    Country = shop.Location.Country,
                    County = shop.Location.County,
                    Longitude = shop.Location.Longitude,
                    Street = shop.Location.Street,
                    StreetNumber = shop.Location.StreetNumber
                };

                Db.Locations.Add(newShopLocation);

                var newShop = new Shop
                {
                    DateCreated = DateTime.Now.ToUniversalTime(),
                    DateModified = DateTime.Now.ToUniversalTime(),
                    Email = shop.Email,
                    Password = Helper.Hash(shop.Password),
                    Deleted = false,
                    LegalName = shop.LegalName,
                    ShortName = shop.ShortName,
                    Vat = shop.Vat,
                    Location = newShopLocation,
                    WorkHours = "08:00-16:00",
                    WorkDays = "0111110"
                    
                };

                Db.Shops.Add(newShop);
                Db.SaveChanges();

                return CreateOkResponse();
            }
            catch (Exception e)
            {
                return CreateErrorResponse(e, ErrorCodeEnum.UnexpectedError);
            }
        }
    }
}
