using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using WebAPI.Models.DTOs;
using WebAPI.Models.Helpers;

namespace WebAPI.Models.ORM
{
    partial class User
    {
        public static UserDTO ToDTO(User item, bool singleLevel = true)
        {
            if (item == null)
            {
                return null;
            }

            return new UserDTO
            {
                DateCreated = item.DateCreated,
                DateDeleted = item.DateDeleted,
                DateModified = item.DateModified,
                Deleted = item.Deleted,
                Id = item.Id,
                Tokens = singleLevel ? null : Token.ToListDTO(item.Tokens),
                Requests = singleLevel ? null : Request.ToListDTO(item.Requests),
                Reviews = singleLevel ? null : Review.ToListDTO(item.Reviews),
                Cars = singleLevel ? null : Car.ToListDTO(item.Cars),
                Locations = singleLevel ? null : Location.ToListDTO(item.Locations),
                DateOfBirth = item.DateOfBirth,
                Email = item.Email,
                FirstName = item.FirstName,
                LastName = item.LastName,
                Username = item.Username,
                Password = Helper.PASSWORD_PLACEHOLDER
            };
        }

        public static ICollection<UserDTO> ToListDTO(ICollection<User> list, bool singleLevel = true)
        {
            if (list == null)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(singleLevel)).ToList();
        }

        public UserDTO ToDTO(bool singleLevel = true)
        {
            return new UserDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Tokens = singleLevel ? null : Token.ToListDTO(Tokens, false),
                Requests = singleLevel ? null : Request.ToListDTO(Requests),
                Reviews = singleLevel ? null : Review.ToListDTO(Reviews),
                Cars = singleLevel ? null : Car.ToListDTO(Cars),
                Locations = singleLevel ? null : Location.ToListDTO(Locations),
                DateOfBirth = DateOfBirth,
                Email = Email,
                FirstName = FirstName,
                LastName = LastName,
                Username = Username,
                Password = Helper.PASSWORD_PLACEHOLDER
            };
        }
    }
}