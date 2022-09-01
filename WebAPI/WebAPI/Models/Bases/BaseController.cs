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

        public User AuthUser
        {
            get
            {
                if (_user != null)
                {
                    return _user;
                }

                if (_user == null && User?.Identity?.Name != null)
                {
                    _user = _db.Users.Where(u => u.Username == User.Identity.Name).FirstOrDefault();
                    return _user;
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
        public SingleResponse<object> CreateOkResponse()
        {
            return new SingleResponse<object>
            {
                Data = null,
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
            Log(SeverityEnum.Error, message);

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
            Log(AuthUser?.Id ?? -1, application, severity, message, timestamp);

        }

        [NonAction]
        public void Log(int userId, ApplicationEnum application, SeverityEnum severity, string message, DateTime timestamp)
        {
            Log(userId, application, severity, _controllerName, message, timestamp);

        }

        [NonAction]
        public void Log(int userId, SeverityEnum severity, string message, DateTime timestamp)
        {
            Log(userId, ApplicationEnum.API, severity, _controllerName, message, timestamp);
        }

        [NonAction]
        public void Log(int userId, ApplicationEnum application, SeverityEnum severity, string source, string message, DateTime timestamp)
        {
            Db.Logs.Add(new Log
            {
                UserId = userId,
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