using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Web.Http;
using WebAPI.Auth;
using WebAPI.Models.Enums;
using WebAPI.Models.Exceptions;
using WebAPI.Models.ORM;
using WebAPI.Models.Responses;

namespace WebAPI.Models
{
    [BasicAuthentication]
    public abstract class BaseController : ApiController
    {
        internal readonly string _controllerName;
        internal readonly DbEntities _db;
        internal User _user;
        internal Shop _shop;

        public User AuthUser
        {
            get
            {
                if (_user != null)
                {
                    return _user;
                }

                if (_user == null && User?.Identity?.AuthenticationType == "User" && User?.Identity?.Name != null)
                {
                    _user = _db.Users.Where(u => u.Username == User.Identity.Name).FirstOrDefault();
                    return _user;
                }

                return null;
            }
        }

        public Shop AuthShop
        {
            get
            {
                if (_shop != null)
                {
                    return _shop;
                }

                if (_shop == null && User?.Identity?.AuthenticationType == "Shop" && User?.Identity?.Name != null)
                {
                    _shop = _db.Shops.Where(u => u.Email == User.Identity.Name).FirstOrDefault();
                    return _shop;
                }

                return null;
            }
        }

        public DbEntities Db
        {
            get
            {
                return _db;
            }
        }

        public BaseController(string controllerName)
        {
            _controllerName = controllerName;
            _db = new DbEntities();
        }

        #region Responses

        [NonAction]
        public ListResponse<T> CreateOkResponse<T>(List<T> data)
        {
            return new ListResponse<T>
            {
                Data = data,
                Message = "Success!",
                Status = HttpStatusCode.OK,
                IsSuccess = true
            };
        }

        [NonAction]
        public SingleResponse<T> CreateOkResponse<T>(T data)
        {
            return new SingleResponse<T>
            {
                Data = data,
                Message = "Success!",
                Status = HttpStatusCode.OK,
                IsSuccess = true
            };
        }

        [NonAction]
        public BaseResponse CreateOkResponse()
        {
            return new BaseResponse
            {
                Message = "Success!",
                Status = HttpStatusCode.OK,
                IsSuccess = true
            };
        }

        [NonAction]
        public PaginatedResponse<T> CreateOkResponse<T>(List<T> data, int page, int pageSize, int total)
        {
            return new PaginatedResponse<T>
            {
                Data = data,
                Page = page,
                PageSize = pageSize,
                Total = total,
                Message = "Success!",
                Status = HttpStatusCode.OK,
                IsSuccess = true
            };
        }

        [NonAction]
        public ErrorResponse CreateErrorResponse(Exception e, ErrorCodeEnum errorCode)
        {
            return CreateErrorResponse(e.Message, errorCode);
        }

        [NonAction]
        public ErrorResponse CreateErrorResponse(string message, ErrorCodeEnum errorCode)
        {
            if (!errorCode.Equals(ErrorCodeEnum.RecordNotFound))
            {
                Log(SeverityEnum.Error, message);
            }

            return new ErrorResponse
            {
                Message = message,
                Status = DetermineStatusFromErrorCode(errorCode) ?? HttpStatusCode.InternalServerError,
                ErrorCode = errorCode,
                IsSuccess = false
            };
        }

        [NonAction]
        private HttpStatusCode? DetermineStatusFromErrorCode(ErrorCodeEnum errorCode)
        {

            //TODO : implement proper error code switch
            return null;
        }

        #endregion

        #region Log

        [NonAction]
        public void Log(SeverityEnum severity, string message)
        {
            Log(severity, message, DateTime.Now);

        }

        [NonAction]
        public void Log(SeverityEnum severity, string message, DateTime timestamp)
        {
            Log(ApplicationEnum.API, severity, message, timestamp);

        }

        [NonAction]
        public void Log(ApplicationEnum application, SeverityEnum severity, string message, DateTime timestamp)
        {
            Log( application, severity, message, timestamp, AuthUser?.Id, AuthShop?.Id);

        }

        [NonAction]
        public void Log( ApplicationEnum application, SeverityEnum severity, string message, DateTime timestamp, int? userId = null, int? shopId = null)
        {
            Log(application, severity, _controllerName, message, timestamp, userId, shopId);

        }

        [NonAction]
        public void Log(SeverityEnum severity, string message, DateTime timestamp, int? userId = null, int? shopId = null )
        {
            Log( ApplicationEnum.API, severity, _controllerName, message, timestamp,userId, shopId);
        }

        [NonAction]
        public void Log(ApplicationEnum application, SeverityEnum severity, string source, string message, DateTime timestamp, int? userId = null, int? shopId = null)
        {
            Db.Logs.Add(new Log
            {
                UserId = userId,
                ShopId = shopId,
                Application = Enum.GetName(typeof(ApplicationEnum), application),
                Severity = Enum.GetName(typeof(SeverityEnum), severity),
                Source = source.Length > 200 ? source.Substring(0, 199) : source,
                Message = message,
                Timestamp = timestamp
            });
            Db.SaveChanges();
        }
        #endregion
    }
}