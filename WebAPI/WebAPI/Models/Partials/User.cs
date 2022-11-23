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
        public static UserDTO ToDTO(User item, int level)
        {
            if (item == null || level <= 0)
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
                Tokens = Token.ToListDTO(item.Tokens, level - 1),
                Requests = Request.ToListDTO(item.Requests, level - 1),
                Reviews = Review.ToListDTO(item.Reviews, level - 1),
                Cars = Car.ToListDTO(item.Cars, level - 1),
                Locations = Location.ToListDTO(item.Locations, level - 1),
                DateOfBirth = item.DateOfBirth,
                Email = item.Email,
                FirstName = item.FirstName,
                LastName = item.LastName,
                Username = item.Username,
                Password = Helper.PASSWORD_PLACEHOLDER
            };
        }

        public static ICollection<UserDTO> ToListDTO(ICollection<User> list, int level)
        {
            if (list == null || level <= 0)
            {
                return null;
            }

            return list.Select(x => x.ToDTO(level)).ToList();
        }

        public UserDTO ToDTO(int level)
        {
            return level > 0 ? new UserDTO
            {
                DateCreated = DateCreated,
                DateDeleted = DateDeleted,
                DateModified = DateModified,
                Deleted = Deleted,
                Id = Id,
                Tokens = Token.ToListDTO(Tokens, level - 1),
                Requests = Request.ToListDTO(Requests, level - 1),
                Reviews = Review.ToListDTO(Reviews, level - 1),
                Cars = Car.ToListDTO(Cars, level - 1),
                Locations = Location.ToListDTO(Locations, level - 1),
                DateOfBirth = DateOfBirth,
                Email = Email,
                FirstName = FirstName,
                LastName = LastName,
                Username = Username,
                Password = Helper.PASSWORD_PLACEHOLDER
            } : null;
        }
    }
}